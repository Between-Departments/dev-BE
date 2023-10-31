package com.gwakkili.devbe.reply.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Schema
public abstract class ReplyDto {

    @Schema(description = "댓글 번호", example = "5")
    private long replyId;

    @Schema(description = "게시글 번호", example = "1")
    private long postId;

    @Schema(description = "게시글 내용", example = "안녕하세요~")
    private String content;

}
