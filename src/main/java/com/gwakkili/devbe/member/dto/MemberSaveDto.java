package com.gwakkili.devbe.member.dto;


import com.gwakkili.devbe.image.dto.ImageDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;

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
    @NotBlank
    @Schema(description = "메일", example = "sjm9600@sun.ac.kr")
    private String mail;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    @Schema(description = "비밀번호", example = "Aa12345678!")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    @Schema(description = "비밀번호 확인", example = "Aa12345678!")
    private String passwordConfirm;

    @NotBlank
    @NicknameDuplicate
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{1,8}$")
    @Schema(description = "닉네임", example = "과끼리123")
    private String nickname;

    @School
    @NotBlank
    @Schema(description = "학교", example = "서울대학교")
    private String school;

    @Major
    @NotBlank
    @Schema(description = "전공", example = "컴퓨터공학과")
    private String major;

    @Valid
    private ImageDto image;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .mail(mail)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .school(school)
                .major(major)
                .build();
        member.addRole(Member.Role.ROLE_USER);
        member.setImage(image.dtoToMemberImage());
        return member;
    }
}
