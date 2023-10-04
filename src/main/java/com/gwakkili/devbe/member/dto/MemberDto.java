package com.gwakkili.devbe.member.dto;

import com.gwakkili.devbe.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class MemberDto {

    @Schema(description = "회원 번호", example = "1")
    private long memberId;

    @Schema(description = "메일", example = "sjm9600@sun.ac.kr")
    private String mail;

    @Schema(description = "닉네임", example = "닉네임이다")
    private String nickname;

    @Schema(description = "대학교", example = "서울대학교")
    private String school;

    @Schema(description = "전공", example = "컴퓨터공학과")
    private String major;

    @Schema(description = "권한", example = "ROLE_USER")
    private Set<Member.Role> roles = new HashSet<>();

    @Schema(description = "프로필 이미지 url", example = "http://example.com/images/image.jpa")
    private String imageUrl;

    @Schema(description = "가입일")
    private LocalDateTime createAt;

    @Schema(description = "변경일")
    private LocalDateTime updateAt;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .mail(member.getMail())
                .nickname(member.getNickname())
                .major(member.getMajor())
                .school(member.getSchool())
                .imageUrl(member.getImage().getUrl())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .build();
    }
}
