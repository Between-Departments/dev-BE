package com.gwakkili.devbe.event;

import com.gwakkili.devbe.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewReplyReportEvent {
    private Member member;

    private String content;

    private Long postId;

    private Long replyId;

    @Builder
    public NewReplyReportEvent(Member member, String content, Long postId, Long replyId) {
        this.member = member;
        this.content = content;
        this.postId = postId;
        this.replyId = replyId;
    }
}
