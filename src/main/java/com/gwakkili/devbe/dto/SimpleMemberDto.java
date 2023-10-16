package com.gwakkili.devbe.dto;

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
}
