package com.gwakkili.devbe.image.entity;

import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String imageUrl;

    private String thumbnailUrl;

    public void setMember(Member member) {
        this.member = member;
    }
}
