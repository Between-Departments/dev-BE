package com.gwakkili.devbe.chat.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.chat.entity.RecentChatMessage;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema
public class ChatRoomDto {

    @Schema(description = "채팅방 번호", example = "1")
    private long chatRoomId;

    @Schema(description = "채팅 상대")
    private SimpleMemberDto member;

    @Schema(description = "최근 채팅 메시지")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ChatMessageDto recentChatMessage;

    @Schema(description = "않읽은 메시지 존재 여부")
    private boolean existNotRead;

    public static ChatRoomDto of(ChatRoom chatRoom, RecentChatMessage recentChatMessage, long memberId) {

        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .member(new SimpleMemberDto(memberId == chatRoom.getMaster().getMemberId() ? chatRoom.getMember() : chatRoom.getMaster(), false))
                .recentChatMessage(
                        ChatMessageDto.builder()
                                .chatMessageId(recentChatMessage.getChatMessageId())
                                .content(
                                        recentChatMessage.getContent().length() > 40 ?
                                                recentChatMessage.getContent().substring(0, 40) + "..." :
                                                recentChatMessage.getContent()
                                )
                                .createAt(recentChatMessage.getCreateAt())
                                .build()
                )
                .existNotRead(recentChatMessage.getSenderId() != memberId && !recentChatMessage.isRead())
                .build();
    }
}
