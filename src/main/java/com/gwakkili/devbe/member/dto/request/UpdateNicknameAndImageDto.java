package com.gwakkili.devbe.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.NicknameDuplicate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class UpdateNicknameAndImageDto {

    @JsonIgnore
    long memberId;

    @NotBlank
    @NicknameDuplicate
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{1,8}$")
    @Schema(description = "닉네임", example = "하이디")
    private String nickname;


    @NotBlank
    @URL
    @Schema(description = "프로필 이미지 URL", example = "http://example.com/images/image.jpa")
    private String imageUrl;
}
