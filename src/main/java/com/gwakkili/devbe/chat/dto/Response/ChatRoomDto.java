package com.gwakkili.devbe.chat.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.chat.entity.RecentChatMessage;
import com.gwakkili.devbe.dto.SimpleMemberDto;
import com.gwakkili.devbe.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChatRoomDto {

    @Schema(description = "채팅방 번호", example = "1")
    private long chatRoomId;

    @Schema(description = "채팅 상대")
    private SimpleMemberDto member;

    @Schema(description = "최근 채팅 메시지")
    private ChatMessageDto recentChatMessage;

    @Schema(description = "않읽은 메시지 존재 여부")
    boolean existNotRead;

    public static ChatRoomDto of(ChatRoom chatRoom, RecentChatMessage recentChatMessage, boolean isMaster) {

        Member member = (isMaster) ? chatRoom.getMember() : chatRoom.getMaster();
        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .member(
                        SimpleMemberDto.builder()
                                .memberId(member.getMemberId())
                                .nickname(member.getNickname())
                                .imageUrl(member.getImage().getThumbnailUrl())
                                .build()
                )
                .recentChatMessage(
                        ChatMessageDto.builder()
                                .chatMessageId(recentChatMessage.getChatMessageId())
                                .content(recentChatMessage.getContent())
                                .createAt(recentChatMessage.getCreateAt())
                                .build()
                )
                .existNotRead(!recentChatMessage.isRead())
                .build();
    }

    public static ChatRoomDto of(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .member(
                        SimpleMemberDto.builder()
                                .memberId(chatRoom.getMember().getMemberId())
                                .nickname(chatRoom.getMember().getNickname())
                                .imageUrl(chatRoom.getMember().getImage().getThumbnailUrl())
                                .build()
                )
                .build();
    }
}
