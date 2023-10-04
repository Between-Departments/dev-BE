package com.gwakkili.devbe.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.image.dto.ImageDto;
import com.gwakkili.devbe.validation.NicknameDuplicate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class NicknameAndImageDto {

    @JsonIgnore
    String mail;

    @NotBlank
    @NicknameDuplicate
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{1,8}$")
    @Schema(description = "닉네임", example = "과끼리123")
    String nickname;


    @Schema(description = "프로필 이미지 url 및 썸네일 url")
    @Valid
    ImageDto image;
}
