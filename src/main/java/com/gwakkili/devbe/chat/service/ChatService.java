package com.gwakkili.devbe.chat.service;

import com.gwakkili.devbe.chat.dto.Request.SaveChatMessageDto;
import com.gwakkili.devbe.chat.dto.Request.SaveChatRoomDto;
import com.gwakkili.devbe.chat.dto.Response.ChatMessageDto;
import com.gwakkili.devbe.chat.dto.Response.ChatRoomDto;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;

import java.util.List;

public interface ChatService {

    ChatRoomDto saveChatRoom(SaveChatRoomDto saveChatRoomDto);

    SliceResponseDto<ChatRoomDto, Object[]> getChatRoomList(long memberId, SliceRequestDto sliceRequestDto);

    void deleteChatRoom(long roomId, long memberId);

    ChatMessageDto saveChatMessage(SaveChatMessageDto saveChatMessageDto, int MemberNumInRoom);

    void updateChatMessageIsRead(long roomId, long memberId);

    SliceResponseDto<ChatMessageDto, ChatMessage> getChatMessageList(long roomId, long memberId, SliceRequestDto sliceRequestDto);
}
