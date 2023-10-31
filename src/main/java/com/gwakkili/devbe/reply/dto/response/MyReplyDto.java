package com.gwakkili.devbe.reply.dto.response;

import com.gwakkili.devbe.reply.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MyReplyDto extends ReplyDto {

    @Schema(description = "생성일")
    private LocalDateTime createAt;

    @Schema(description = "변경일")
    private LocalDateTime updateAt;

    public static MyReplyDto of(Reply reply) {
        return MyReplyDto.builder()
                .replyId(reply.getReplyId())
                .postId(reply.getPost().getPostId())
                .createAt(reply.getCreateAt())
                .updateAt(reply.getUpdateAt())
                .content(reply.getContent())
                .build();
    }
}
