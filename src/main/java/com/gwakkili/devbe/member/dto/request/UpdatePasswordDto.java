package com.gwakkili.devbe.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.PasswordConfirm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@PasswordConfirm(fieldName1 = "newPassword", fieldName2 = "newPasswordConfirm")
@Schema
public class UpdatePasswordDto {

    @JsonIgnore
    long memberId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    @Schema(description = "새로운 비밀번호", example = "Aa12345678!")
    private String newPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    @Schema(description = "비밀번호 확인", example = "Aa12345678!")
    private String newPasswordConfirm;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    @Schema(description = "예전 비밀번호", example = "bb45674567!")
    private String password;
}
