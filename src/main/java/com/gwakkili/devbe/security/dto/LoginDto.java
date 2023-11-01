package com.gwakkili.devbe.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema
public class LoginDto {

    @Schema(description = "메일", example = "abcd1234@sun.ac.kr")
    private String mail;

    @Schema(description = "비밀번호", example = "abc1231234!")
    private String password;
}
