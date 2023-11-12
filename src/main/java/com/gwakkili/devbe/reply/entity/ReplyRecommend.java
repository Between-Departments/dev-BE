package com.gwakkili.devbe.reply.entity;

import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reply_recommend",
        uniqueConstraints = @UniqueConstraint(name = "unique_member_and_reply", columnNames = {"member_id", "reply_id"})
)
public class ReplyRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyRecommendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reply reply;

    @Builder
    public ReplyRecommend(Member member, Reply reply) {
        this.member = member;
        this.reply = reply;
    }
}

