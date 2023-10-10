package com.gwakkili.devbe.reply.entity;

import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplyRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ReplyRecommendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;

    @Builder
    public ReplyRecommend(long ReplyRecommendId, Member member, Reply reply) {
        this.ReplyRecommendId = ReplyRecommendId;
        this.member = member;
        this.reply = reply;
    }
}
