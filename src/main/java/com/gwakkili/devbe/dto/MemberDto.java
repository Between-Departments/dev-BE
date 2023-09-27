package com.gwakkili.devbe.dto;

import com.gwakkili.devbe.validation.MailAuth;
import com.gwakkili.devbe.validation.Major;
import com.gwakkili.devbe.validation.PasswordConfirm;
import com.gwakkili.devbe.validation.School;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@PasswordConfirm(fieldName1 = "password", fieldName2 = "passwordConfirm")
public class MemberDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class Save{

        @Email
        @NotBlank
        @MailAuth
        private String mail;

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
        private String password;

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
        private String passwordConfirm;

        @NotBlank
        private String nickname;

        @School
        @NotBlank
        private String school;

        @Major
        @NotBlank
        private String major;

        private String imageUrl;

    }
}
