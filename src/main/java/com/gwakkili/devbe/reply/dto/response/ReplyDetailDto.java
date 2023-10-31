package com.gwakkili.devbe.reply.dto.response;

import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.reply.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@Schema
@EqualsAndHashCode(callSuper = true)
public class ReplyDetailDto extends ReplyDto {

    private long postWriterId;

    private SimpleMemberDto writer;

    @Schema(description = "추천 개수", example = "3")
    private int recommendCount;

    @Schema(description = "추천 여부")
    private boolean isRecommend;

    @Schema(description = "익명 여부")
    private boolean isAnonymous;

    @Schema(description = "나의 댓글 여부")
    private boolean isMine;

    @Schema(description = "생성일")
    private LocalDateTime createAt;

    @Schema(description = "변경일")
    private LocalDateTime updateAt;


    public static ReplyDetailDto of(Reply reply, boolean isRecommend, boolean isMine) {

        return ReplyDetailDto.builder()
                .replyId(reply.getReplyId())
                .postId(reply.getPost().getPostId())
                .postWriterId(reply.getPost().getWriter().getMemberId())
                .writer(new SimpleMemberDto(reply.getMember(), reply.isAnonymous()))
                .content(reply.getContent())
                .recommendCount(reply.getRecommendCount())
                .createAt(reply.getCreateAt())
                .updateAt(reply.getUpdateAt())
                .isAnonymous(reply.isAnonymous())
                .isRecommend(isRecommend)
                .isMine(isMine)
                .build();
    }
}
