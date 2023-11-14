package com.gwakkili.devbe.notification.entity;

import com.gwakkili.devbe.entity.BaseEntity;
import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    private Member member;

    private Long postId;

    @Column(nullable = true)
    private Long replyId;

    private String content;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Builder
    public Notification(Member member, Long postId, Long replyId, String content, Type type) {
        this.member = member;
        this.postId = postId;
        this.replyId = replyId;
        this.content = content;
        this.type = type;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        REPLY("댓글 알림"),
        POST_REPORT("게시물 신고 알림"),
        REPLY_REPORT("댓글 신고 알림");

        private final String description;
    }



}



