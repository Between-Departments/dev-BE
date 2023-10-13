package com.gwakkili.devbe.reply.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.AccessDeniedException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.repository.PostRepository;
import com.gwakkili.devbe.reply.dto.*;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.entity.ReplyRecommend;
import com.gwakkili.devbe.reply.repository.ReplyRecommendRepository;
import com.gwakkili.devbe.report.dto.ReplyReportDto;
import com.gwakkili.devbe.report.repository.ReplyReportRepository;
import com.gwakkili.devbe.reply.repository.ReplyRepository;
import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.security.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final ReplyRecommendRepository replyRecommendRepository;

    private final MemberRepository memberRepository;

    private final PostRepository postRepository;

    private final ReplyReportRepository replyReportRepository;


    @Override
    public ReplyDto saveReply(ReplySaveDto replySaveDto) {

        Member writer = memberRepository.getReferenceById(replySaveDto.getPostId());
        Post post = postRepository.getReferenceById(replySaveDto.getPostId());

        Reply reply = Reply.builder()
                .post(post)
                .member(writer)
                .content(replySaveDto.getContent())
                .isAnonymous(replySaveDto.isAnonymous())
                .build();

        Reply saveReply = replyRepository.save(reply);
        return ReplyDto.of(saveReply);
    }


    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ReplyDto, Reply> getReplyList(long postId, SliceRequestDto sliceResponseDto) {

        Post post = postRepository.getReferenceById(postId);
        Slice<Reply> replyList = replyRepository.findByPost(post, sliceResponseDto.getPageable());
        if (replyList.getNumberOfElements() == 0) throw new NotFoundException(ExceptionCode.NOT_FOUND_REPLY);
        Function<Reply, ReplyDto> fn = (ReplyDto::of);
        return new SliceResponseDto(replyList, fn);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ReplyDto, Reply> getMyReplyList(long memberId, SliceRequestDto sliceResponseDto) {

        Member writer = memberRepository.getReferenceById(memberId);
        Slice<Reply> replyList = replyRepository.findByMember(writer, sliceResponseDto.getPageable());
        if (replyList.getNumberOfElements() == 0) throw new NotFoundException(ExceptionCode.NOT_FOUND_REPLY);
        Function<Reply, ReplyDto> fn = (ReplyDto::of);
        return new SliceResponseDto(replyList, fn);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ReportedReplyDto, Object[]> getReportedReplyList(SliceRequestDto sliceRequestDto) {
        Slice<Object[]> replyList = replyRepository.findReported(sliceRequestDto.getPageable());
        if (replyList.getNumberOfElements() == 0) throw new NotFoundException(ExceptionCode.NOT_FOUND_REPLY);
        Function<Object[], ReportedReplyDto> fn = (object -> ReportedReplyDto.of((Reply) object[0], (long) object[1]));
        return new SliceResponseDto(replyList, fn);
    }


    @Override
    public ReplyDto updateReply(ReplyUpdateDto replyUpdateDto) {

        Reply reply = replyRepository.findById(replyUpdateDto.getReplyId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_REPLY));

        // 현재 요청한 사용자가 글쓴이가 아닐경우
        if (reply.getMember().getMemberId() != replyUpdateDto.getMemberId())
            throw new AccessDeniedException(ExceptionCode.ACCESS_DENIED);

        reply.setContent(replyUpdateDto.getContent());

        return ReplyDto.of(reply);
    }

    @Override
    public void deleteReply(MemberDetails memberDetails, long replyId) {

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_REPLY));

        // 사용자 권한이 매니저가 아니고 글쓴이가 아닐 경우
        if (!memberDetails.getRoles().contains(Member.Role.ROLE_MANAGER) &&
                reply.getMember().getMemberId() != memberDetails.getMemberId())
            throw new AccessDeniedException(ExceptionCode.ACCESS_DENIED);

        replyRepository.delete(reply);
    }


    @Override
    public void recommendReply(long memberId, long replyId) {

        Member member = memberRepository.getReferenceById(memberId);
        Reply reply = replyRepository.getReferenceById(replyId);

        replyRecommendRepository.findByMemberAndReply(member, reply)
                .ifPresentOrElse(replyRecommendRepository::delete,
                        () -> {
                            ReplyRecommend replyRecommend = ReplyRecommend.builder()
                                    .reply(reply)
                                    .member(member)
                                    .build();
                            replyRecommendRepository.save(replyRecommend);
                        }
                );
    }
}
