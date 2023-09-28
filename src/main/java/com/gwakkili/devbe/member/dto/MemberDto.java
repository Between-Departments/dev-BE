package com.gwakkili.devbe.member.dto;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.validation.PasswordConfirm;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@PasswordConfirm(fieldName1 = "password", fieldName2 = "passwordConfirm")
public class MemberDto {

    private long memberId;

    private String mail;

    private String nickname;

    private String school;

    private String major;

    private Set<Member.Role> roles = new HashSet<>();

    private String imageUrl;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    public static MemberDto of(Member member){
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .mail(member.getMail())
                .nickname(member.getNickname())
                .major(member.getMajor())
                .school(member.getSchool())
                .imageUrl(member.getImageUrl())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .build();
    }
}
