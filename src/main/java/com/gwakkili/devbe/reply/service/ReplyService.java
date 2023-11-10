package com.gwakkili.devbe.reply.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.reply.dto.request.ReplySaveDto;
import com.gwakkili.devbe.reply.dto.request.ReplyUpdateDto;
import com.gwakkili.devbe.reply.dto.response.MyReplyDto;
import com.gwakkili.devbe.reply.dto.response.ReplyDetailDto;
import com.gwakkili.devbe.reply.dto.response.ReportedReplyDto;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.security.dto.MemberDetails;

public interface ReplyService {

    ReplyDetailDto saveReply(ReplySaveDto replySaveDto);

    SliceResponseDto<ReplyDetailDto, Reply> getReplyList(long postId, SliceRequestDto sliceResponseDto);

    SliceResponseDto<ReplyDetailDto, Object[]> getReplyList(long postId, long memberId, SliceRequestDto sliceRequestDto);

    SliceResponseDto<MyReplyDto, Reply> getMyReplyList(long memberId, SliceRequestDto sliceResponseDto, Post.BoardType boardType);

    SliceResponseDto<ReportedReplyDto, Object[]> getReportedReplyList(SliceRequestDto sliceRequestDto);

    ReplyDetailDto updateReply(ReplyUpdateDto replyUpdateDto);

    void deleteReply(MemberDetails memberDetails, long replyId);

    void recommendReply(long memberId, long replyId);
}
