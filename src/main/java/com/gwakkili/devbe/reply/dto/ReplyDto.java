package com.gwakkili.devbe.reply.dto;

import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.reply.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class ReplyDto {

    @Schema(description = "댓글 번호", example = "5")
    private long replyId;

    @Schema(description = "게시글 번호", example = "1")
    private long postId;

    private SimpleMemberDto writer;

    @Schema(description = "게시글 내용", example = "안녕하세요~")
    private String content;

    @Schema(description = "추천 개수", example = "3")
    private int recommendCount;

    @Schema(description = "생성일")
    private LocalDateTime createAt;

    @Schema(description = "변경일")
    private LocalDateTime updateAt;

    @Schema(description = "익명 여부")
    private boolean isAnonymous;

    @Data
    @AllArgsConstructor
    @Schema
    public static class Writer {

        @Schema(description = "닉네임", example = "하이디1")
        String nickname;

        @Schema(description = "프로필 이미지 url", example = "http://example.com/images/image.jpg")
        String imageUrl;
    }

    public static ReplyDto of(Reply reply) {
        return ReplyDto.builder()
                .replyId(reply.getReplyId())
                .postId(reply.getPost().getPostId())
                .writer(
                        SimpleMemberDto.builder()
                                .memberId(reply.getMember().getMemberId())
                                .imageUrl(reply.getMember().getImage().getThumbnailUrl())
                                .nickname(reply.getMember().getNickname())
                                .build()
                )
                .content(reply.getContent())
                .recommendCount((reply.getRecommendCount() == null) ? 0 : reply.getRecommendCount())
                .createAt(reply.getCreateAt())
                .updateAt(reply.getUpdateAt())
                .build();
    }
}
