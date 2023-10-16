package com.gwakkili.devbe.chat.dto.Response;

import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoomDto {

    private long ChatRoomId;

    private ChatMessageDto recentChatMessage;

    public static ChatRoomDto of(ChatRoom chatRoom, ChatMessage chatMessage) {
        return ChatRoomDto.builder()
                .ChatRoomId(chatRoom.getChatRoomId())
                .recentChatMessage(
                        ChatMessageDto.builder()
                                .chatMessageId(chatMessage.getChatMessageId())
                                .chatRoomId(chatMessage.getChatRoom().getChatRoomId())
                                .sender(SimpleMemberDto.builder()
                                        .memberId(chatMessage.getSender().getMemberId())
                                        .nickname(chatMessage.getSender().getNickname())
                                        .imageUrl(chatMessage.getSender().getImage().getThumbnailUrl())
                                        .build()
                                )
                                .content(chatMessage.getContent())
                                .createAt(chatMessage.getCreateAt())
                                .build()
                )
                .build();
    }

    public static ChatRoomDto of(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .ChatRoomId(chatRoom.getChatRoomId())
                .build();
    }
}
