package com.gwakkili.devbe.reply.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.event.DeleteByManagerEvent;
import com.gwakkili.devbe.event.DeleteMemberEvent;
import com.gwakkili.devbe.event.DeletePostEvent;
import com.gwakkili.devbe.event.DeleteReplyEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.repository.PostRepository;
import com.gwakkili.devbe.reply.dto.request.ReplySaveDto;
import com.gwakkili.devbe.reply.dto.request.ReplyUpdateDto;
import com.gwakkili.devbe.reply.dto.response.MyReplyDto;
import com.gwakkili.devbe.reply.dto.response.ReplyDetailDto;
import com.gwakkili.devbe.reply.dto.response.ReportedReplyDto;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.entity.ReplyRecommend;
import com.gwakkili.devbe.reply.repository.ReplyRecommendRepository;
import com.gwakkili.devbe.reply.repository.ReplyRepository;
import com.gwakkili.devbe.security.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final ReplyRecommendRepository replyRecommendRepository;

    private final MemberRepository memberRepository;

    private final PostRepository postRepository;

    private final ApplicationEventPublisher publisher;

    @Override
    public ReplyDetailDto saveReply(ReplySaveDto replySaveDto) {

        Member writer = memberRepository.findWithImageByMemberId(replySaveDto.getWriter())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        Post post = postRepository.getReferenceById(replySaveDto.getPostId());

        Reply reply = Reply.builder()
                .post(post)
                .member(writer)
                .content(replySaveDto.getContent())
                .build();

        Reply saveReply = replyRepository.save(reply);
        return ReplyDetailDto.of(saveReply, false, true);
    }


    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ReplyDetailDto, Reply> getReplyList(long postId, SliceRequestDto sliceResponseDto) {

        Post post = postRepository.getReferenceById(postId);
        Slice<Reply> replyList = replyRepository.findByPost(post, sliceResponseDto.getPageable());
        Function<Reply, ReplyDetailDto> fn = (reply -> ReplyDetailDto.of(reply, false, false));
        return new SliceResponseDto(replyList, fn);
    }

    @Override
    public SliceResponseDto<ReplyDetailDto, Object[]> getReplyList(long postId, long memberId, SliceRequestDto sliceRequestDto) {
        Post post = postRepository.getReferenceById(postId);
        Member member = memberRepository.getReferenceById(memberId);
        Slice<Object[]> replyList = replyRepository.findWithRecommendByPostAndMember(post, member, sliceRequestDto.getPageable());
        Function<Object[], ReplyDetailDto> fn = (objects -> {
            Reply reply = (Reply) objects[0];
            boolean isRecommend = (boolean) objects[1];
            boolean isMine = reply.getMember().getMemberId() == memberId;
            return ReplyDetailDto.of(reply, isRecommend, isMine);
        });
        return new SliceResponseDto<>(replyList, fn);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<MyReplyDto, Reply> getMyReplyList(long memberId, SliceRequestDto sliceResponseDto) {

        Member writer = memberRepository.getReferenceById(memberId);
        Slice<Reply> replyList = replyRepository.findByMember(writer, sliceResponseDto.getPageable());
        if (replyList.getNumberOfElements() == 0) throw new NotFoundException(ExceptionCode.NOT_FOUND_REPLY);
        Function<Reply, MyReplyDto> fn = (MyReplyDto::of);
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
    public ReplyDetailDto updateReply(ReplyUpdateDto replyUpdateDto) {

        long replyId = replyUpdateDto.getReplyId();
        Member member = memberRepository.getReferenceById(replyUpdateDto.getMemberId());
        Object[] objects = replyRepository.findWithRecommendByIdAndMember(replyId, member)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_REPLY));
        Reply reply = (Reply) objects[0];
        boolean isRecommend = (boolean) objects[1];
        // 현재 요청한 사용자가 글쓴이가 아닐경우
        if (reply.getMember().getMemberId() != replyUpdateDto.getMemberId())
            throw new AccessDeniedException("접근 거부");

        reply.setContent(replyUpdateDto.getContent());

        return ReplyDetailDto.of(reply, isRecommend, true);
    }

    @Override
    public void deleteReply(MemberDetails memberDetails, long replyId) {

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_REPLY));

        if (memberDetails.getRoles().contains(Member.Role.ROLE_MANAGER)) {
            long memberId = reply.getMember().getMemberId();
            publisher.publishEvent(new DeleteByManagerEvent(memberId));
        } else if (reply.getMember().getMemberId() != memberDetails.getMemberId()) {
            throw new AccessDeniedException("접근 거부");
        }
        replyRepository.delete(reply);
    }

    @EventListener
    public void deleteReply(DeleteMemberEvent deleteMemberEvent) {
        List<Reply> replyList = replyRepository.findByMember(deleteMemberEvent.getMember());
        publisher.publishEvent(new DeleteReplyEvent(replyList));
        // 해당 회원의 다른 댓글 추천 내역 삭제
        replyRecommendRepository.deleteByMember(deleteMemberEvent.getMember());
        //해당 회원이 작성한 댓글의 추천 내역 삭제
        replyRecommendRepository.deleteByReplyIn(replyList);
        // 해당 회원이 작성한 댓글 삭제
        replyRepository.deleteAllInBatch(replyList);
    }

    @EventListener
    public void deleteReply(DeletePostEvent deletePostEvent) {
        List<Reply> replyList = replyRepository.findByPostIn(deletePostEvent.getPostList());
        publisher.publishEvent(new DeleteReplyEvent(replyList));
        //댓글 추천 내역 삭제
        replyRecommendRepository.deleteByReplyIn(replyList);
        //댓글 삭제
        replyRepository.deleteAllInBatch(replyList);
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
