package com.gwakkili.devbe.reply.dto.response;

import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.reply.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@Schema
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ReportedReplyDto extends ReplyDto {


    @Schema(description = "신고 당한 횟수", example = "3")
    private int reportCount;

    private SimpleMemberDto writer;


    public static ReportedReplyDto of(Reply reply, long reportCount) {
        return ReportedReplyDto.builder()
                .replyId(reply.getReplyId())
                .postId(reply.getPost().getPostId())
                .content(reply.getContent())
                .reportCount((int) reportCount)
                .writer(new SimpleMemberDto(reply.getMember(), false))
                .build();
    }
}
