package com.gwakkili.devbe.member.dto.request;


import com.gwakkili.devbe.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@PasswordConfirm(fieldName1 = "password", fieldName2 = "passwordConfirm")
@Schema
public class MemberSaveDto {

    @Email
    @MailDuplicate
    @MailAuth
    @Schema(description = "메일", example = "test1@sun.ac.kr")
    @NotBlank
    private String mail;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    @Schema(description = "비밀번호", example = "Aa12341234!")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    @Schema(description = "비밀번호 홧인", example = "Aa12341234!")
    private String passwordConfirm;

    @NotBlank
    @NicknameDuplicate
    @Schema(description = "닉네임", example = "하이디")
    private String nickname;

    @School
    @NotBlank
    @Schema(description = "학교", example = "서울대학교")
    private String school;

    @Major
    @NotBlank
    @Schema(description = "학과", example = "켐퓨터공학과")
    private String major;

    @NotBlank
    @URL
    @Schema(description = "프로필 이미지 URL", example = "http://example.com/images/image.jpa")
    private String imageUrl;

}
