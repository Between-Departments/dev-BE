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
    long memberId;

    @Schema(description = "닉네임", example = "하이디1")
    String nickname;

    @Schema(description = "프로필 이미지 url", example = "http://example.com/images/image.jpg")
    String imageUrl;

    public SimpleMemberDto(Member member, boolean isAnonymous) {
        this.memberId = member.getMemberId();
        this.nickname = isAnonymous ? "익명" : member.getNickname();
        this.imageUrl = isAnonymous ? "익명 프로픨 이미지 주소" : member.getImage().getThumbnailUrl();
    }
}
