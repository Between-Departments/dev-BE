package com.gwakkili.devbe.member.dto.response;

import com.gwakkili.devbe.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema
public class MemberDto {

    @Schema(description = "회원 번호", example = "1")
    private long memberId;

    @Schema(description = "메일", example = "sjm9600@sun.ac.kr")
    private String mail;

    @Schema(description = "닉네임", example = "닉네임이다")
    private String nickname;

    @Schema(description = "프로필 이미지 url", example = "http://example.com/images/image.jpa")
    private String imageUrl;

    @Schema(description = "가입일")
    private LocalDateTime createAt;

    @Schema(description = "변경일")
    private LocalDateTime updateAt;

    @Schema(description = "정지 여부")
    private boolean locked;

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .memberId(member.getMemberId())
                .mail(member.getMail())
                .nickname(member.getNickname())
                .imageUrl(member.getImage().getThumbnailUrl())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .locked(member.isLocked())
                .build();
    }
}
