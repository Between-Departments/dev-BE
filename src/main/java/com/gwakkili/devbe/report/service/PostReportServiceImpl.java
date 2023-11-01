package com.gwakkili.devbe.report.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.repository.PostRepository;
import com.gwakkili.devbe.report.dto.request.PostReportSaveDto;
import com.gwakkili.devbe.report.dto.response.PostReportDto;
import com.gwakkili.devbe.report.entity.PostReport;
import com.gwakkili.devbe.report.repository.PostReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReportServiceImpl implements PostReportService{

    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    public SliceResponseDto<PostReportDto, PostReport> findPostReportList(long postId, SliceRequestDto sliceRequestDto) {
        Post findPost = postRepository.getReferenceById(postId);

        Slice<PostReport> postReportList = postReportRepository.findByPost(findPost, sliceRequestDto.getPageable());
        Function<PostReport, PostReportDto> fn = (PostReportDto::of);
        return new SliceResponseDto<>(postReportList, fn);
    }

    @Override
    @Transactional
    public void saveNewPostReport(PostReportSaveDto postReportSaveDto, Long postId, long memberId) {
        Member reporter = memberRepository.getReferenceById(memberId);
        Post findPost = find(postId);

        Optional<PostReport> findPostReport = postReportRepository.findByReporterAndPost(reporter, findPost);

        // ! HTTP 409 -> 중복 데이터 존재하는 경우 응답 상태 코드
        findPostReport.ifPresentOrElse(report -> { throw new CustomException(ExceptionCode.DUPLICATE_REPORT); },
                () ->{
                    PostReport newPostReport = PostReport.builder()
                            .reporter(reporter)
                            .post(findPost)
                            .type(postReportSaveDto.getType())
                            .content(postReportSaveDto.getContent())
                            .build();

                    postReportRepository.save(newPostReport);
                });
    }

    @Override
    @Transactional
    public void deletePostReport(long reportId) {
        postReportRepository.deleteById(reportId);
    }


    private Post find(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
    }

}
