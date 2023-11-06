package com.gwakkili.devbe.event;

import com.gwakkili.devbe.member.entity.Member;
import lombok.Getter;

@Getter
public class NewReplyEvent {

    private Member member;

    private String content;

    private Long postId;

    private Long replyId;

    public NewReplyEvent(Member member, String content, Long postId, Long replyId) {
        this.member = member;
        this.content = content;
        this.postId = postId;
        this.replyId = replyId;
    }
}
