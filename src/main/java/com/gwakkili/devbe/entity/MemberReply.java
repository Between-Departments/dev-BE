package com.gwakkili.devbe.entity;

import com.gwakkili.devbe.entity.Member.Member;
import jakarta.persistence.*;
import lombok.Builder;


public class MemberReply {

    @Id
    private long memberPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private int recommendCount;

    private boolean bookmark;

    @Builder
    public MemberReply(long memberPostId, Member member, Post post) {
        this.memberPostId = memberPostId;
        this.member = member;
        this.post = post;
    }
}
