package com.gwakkili.devbe.notification.event;

import lombok.Getter;

@Getter
public class NewReplyEvent {

    private String replyContent;

    public NewReplyEvent(String replyContent) {
        this.replyContent = replyContent;
    }
}
