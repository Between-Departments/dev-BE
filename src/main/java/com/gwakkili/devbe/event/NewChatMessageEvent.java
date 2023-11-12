package com.gwakkili.devbe.event;

import com.gwakkili.devbe.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewChatMessageEvent {

    private String mail;

    private ChatMessage chatMessage;
}
