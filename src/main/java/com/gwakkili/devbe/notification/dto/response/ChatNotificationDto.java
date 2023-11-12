package com.gwakkili.devbe.notification.dto.response;

import com.gwakkili.devbe.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatNotificationDto {


    private final String type = "CHAT_MESSAGE";

    private long chatRoomId;

    private String sender;

    private String content;

    private LocalDateTime createAt;

    public static ChatNotificationDto of(ChatMessage chatMessage) {
        return ChatNotificationDto.builder()
                .chatRoomId(chatMessage.getChatRoom().getChatRoomId())
                .sender(chatMessage.getSender().getNickname())
                .content(chatMessage.getContent().length() > 40 ? chatMessage.getContent().substring(0, 40) + "..." : chatMessage.getContent())
                .createAt(chatMessage.getCreateAt())
                .build();
    }
}
