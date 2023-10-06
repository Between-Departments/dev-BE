package com.gwakkili.devbe.member.dto;


import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@PasswordConfirm(fieldName1 = "password", fieldName2 = "passwordConfirm")
public class MemberSaveDto {

    @Email
    @MailDuplicate
    @MailAuth
    private String mail;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$")
    private String passwordConfirm;

    @NotBlank
    @NicknameDuplicate
    private String nickname;

    @School
    @NotBlank
    private String school;

    @Major
    @NotBlank
    private String major;

    @NotBlank
    @URL
    private String imageUrl;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .mail(mail)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .major(major)
                .school(school)
                .build();
        member.setImage(MemberImage.builder().url(imageUrl).build());
        member.addRole(Member.Role.ROLE_USER);
        return member;
    }

}
