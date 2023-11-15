package com.gwakkili.devbe.member.dto.request;


import com.gwakkili.devbe.validation.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@PasswordConfirm(fieldName1 = "password", fieldName2 = "passwordConfirm")
@MailMissMatch(fieldName1 = "school", fieldName2 = "mail")
@Schema
public class MemberSaveDto {

    @Email
    @MailDuplicate
    @MailNotAuth
    @NotBlank
    @Schema(description = "메일", example = "test1@sun.ac.kr")
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
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{2,8}$")
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
