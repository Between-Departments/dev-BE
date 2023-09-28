package com.gwakkili.devbe.reply.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    private String content;

    private int recommendCount;

    @Builder
    public Reply(long replyId, Member member, Post post, String content) {
        this.replyId = replyId;
        this.member = member;
        this.post = post;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addRecommendCount(){
        this.recommendCount++;
    }

}
