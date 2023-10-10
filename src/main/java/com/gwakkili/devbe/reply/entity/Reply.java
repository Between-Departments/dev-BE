package com.gwakkili.devbe.reply.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String content;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(SELECT count(*) FROM reply_recommend rr WHERE rr.reply_id = reply_id)")
    private Integer recommendCount;

    private boolean isAnonymous;

    public void setContent(String content) {
        this.content = content;
    }

    @Builder
    public Reply(long replyId, Member member, Post post, String content, boolean isAnonymous) {
        this.replyId = replyId;
        this.member = member;
        this.post = post;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }

}
