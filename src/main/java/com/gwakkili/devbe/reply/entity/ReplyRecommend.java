package com.gwakkili.devbe.reply.entity;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;


public class ReplyRecommend {

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
    public ReplyRecommend(long memberPostId, Member member, Post post) {
        this.memberPostId = memberPostId;
        this.member = member;
        this.post = post;
    }
}
