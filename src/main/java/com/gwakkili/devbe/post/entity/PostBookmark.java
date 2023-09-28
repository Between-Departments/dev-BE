package com.gwakkili.devbe.post.entity;

import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private boolean bookmark;

    @Builder
    public PostBookmark(long memberPostId, Member member, Post post) {
        this.memberPostId = memberPostId;
        this.member = member;
        this.post = post;
    }
}
