package com.gwakkili.devbe.chat.dto.Response;

import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatMessageDto {

    private long chatMessageId;

    private long chatRoomId;

    private SimpleMemberDto sender;

    private String content;

    private LocalDateTime createAt;

    public static ChatMessageDto of(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .chatRoomId(chatMessage.getChatRoom().getChatRoomId())
                .sender(
                        SimpleMemberDto.builder()
                                .memberId(chatMessage.getSender().getMemberId())
                                .nickname(chatMessage.getSender().getNickname())
                                .imageUrl(chatMessage.getSender().getImage().getThumbnailUrl())
                                .build()
                )
                .content(chatMessage.getContent())
                .createAt(chatMessage.getCreateAt())
                .build();
    }
}
