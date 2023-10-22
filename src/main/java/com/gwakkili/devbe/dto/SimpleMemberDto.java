package com.gwakkili.devbe.dto;

import com.gwakkili.devbe.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema
@Builder
public class SimpleMemberDto {

    @Schema(description = "회원 번호")
    private Long memberId;

    @Schema(description = "닉네임", example = "하이디1")
    private String nickname;

    @Schema(description = "프로필 이미지 url", example = "http://example.com/images/image.jpg")
    private String imageUrl;

    public SimpleMemberDto(Member member, Boolean isAnonymous) {
        this.memberId = isAnonymous ? null : member.getMemberId();
        this.nickname = isAnonymous ? "익명" : member.getNickname();
        this.imageUrl = isAnonymous ? "https://gwaggiri-bucket.s3.ap-northeast-2.amazonaws.com/images/default_profile.jpg" : member.getImage().getThumbnailUrl();
    }
}
