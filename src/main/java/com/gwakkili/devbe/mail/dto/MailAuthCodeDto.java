package com.gwakkili.devbe.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
@Schema
public class MailAuthCodeDto {

    @Email
    @NotBlank
    @Schema(description = "메일", example = "sjm0709@sun.ac.kr")
    String mail;

    @NotBlank
    @Length(min = 6, max = 6)
    @Schema(description = "인증코드", example = "123456")
    String code;
}
