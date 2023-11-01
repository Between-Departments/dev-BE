package com.gwakkili.devbe.image.entity;

import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private String url;

    public MemberImage(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return url.replace("/images/", "/thumbnails/");
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
