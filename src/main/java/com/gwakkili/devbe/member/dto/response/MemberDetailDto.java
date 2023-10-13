package com.gwakkili.devbe.member.dto.response;

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
public class MemberDetailDto {

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

    @Schema(description = "북마크 개수")
    private int bookmarkCount;

    @Schema(description = "게시글 개수")
    private int postCount;

    @Schema(description = "댓글 개수")
    private int replyCount;

    public static MemberDetailDto of(Member member) {
        return MemberDetailDto.builder()
                .memberId(member.getMemberId())
                .mail(member.getMail())
                .nickname(member.getNickname())
                .school(member.getSchool())
                .major(member.getMajor())
                .imageUrl(member.getImage().getUrl())
                .bookmarkCount(member.getBookmarkCount())
                .postCount(member.getPostCount())
                .replyCount(member.getReplyCount())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .build();
    }
}
