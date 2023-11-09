package com.gwakkili.devbe.chat.dto.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatNotificationDto {

    @JsonIgnore
    private String receiver;

    private final String type = "CHAT_MESSAGE";

    private long chatRoomId;

    private String sender;

    private String content;

    private LocalDateTime createAt;

    public static ChatNotificationDto of(ChatMessage chatMessage, String receiver) {
        return ChatNotificationDto.builder()
                .receiver(receiver)
                .chatRoomId(chatMessage.getChatRoom().getChatRoomId())
                .sender(chatMessage.getSender().getNickname())
                .content(chatMessage.getContent().length() > 40 ? chatMessage.getContent().substring(0, 40) + "..." : chatMessage.getContent())
                .createAt(chatMessage.getCreateAt())
                .build();
    }
}
