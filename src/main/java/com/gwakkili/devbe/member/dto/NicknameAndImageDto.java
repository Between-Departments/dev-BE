package com.gwakkili.devbe.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class NicknameAndImageDto {

    String mail;

    @NotBlank
    String nickname;

    @NotBlank
    @URL
    String imageUrl;
}
