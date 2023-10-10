package com.gwakkili.devbe.reply.dto;

import com.gwakkili.devbe.reply.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class ReportedReplyDto {

    @Schema(description = "댓글 번호", example = "5")
    private long replyId;

    @Schema(description = "댓글 내용", example = "안녕하세요~")
    private String content;

    @Schema(description = "신고 당한 횟수", example = "3")
    private int reportCount;

    private Writer writer;

    @Data
    @AllArgsConstructor
    @Schema
    public static class Writer {

        @Schema(description = "닉네임", example = "하이디1")
        String nickname;

        @Schema(description = "프로필 이미지 url", example = "http://example.com/images/image.jpg")
        String imageUrl;
    }

    public static ReportedReplyDto of(Reply reply, long reportCount) {
        return ReportedReplyDto.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .reportCount((int) reportCount)
                .writer(new Writer(reply.getMember().getNickname(), reply.getMember().getImage().getThumbnailUrl()))
                .build();
    }
}
