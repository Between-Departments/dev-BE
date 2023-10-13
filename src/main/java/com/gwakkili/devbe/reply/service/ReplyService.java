package com.gwakkili.devbe.reply.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.reply.dto.*;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.report.dto.ReplyReportDto;
import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.security.dto.MemberDetails;

public interface ReplyService {

    ReplyDto saveReply(ReplySaveDto replySaveDto);

    SliceResponseDto<ReplyDto, Reply> getReplyList(long postId, SliceRequestDto sliceResponseDto);

    SliceResponseDto<ReplyDto, Reply> getMyReplyList(long memberId, SliceRequestDto sliceResponseDto);

    SliceResponseDto<ReportedReplyDto, Object[]> getReportedReplyList(SliceRequestDto sliceRequestDto);


    ReplyDto updateReply(ReplyUpdateDto replyUpdateDto);

    void deleteReply(MemberDetails memberDetails, long replyId);

    void recommendReply(long memberId, long replyId);
}
