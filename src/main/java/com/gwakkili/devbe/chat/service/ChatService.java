package com.gwakkili.devbe.chat.service;

import com.gwakkili.devbe.chat.dto.Request.SaveChatMessageDto;
import com.gwakkili.devbe.chat.dto.Request.SaveChatRoomDto;
import com.gwakkili.devbe.chat.dto.Response.ChatMessageDto;
import com.gwakkili.devbe.chat.dto.Response.ChatRoomDto;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;

public interface ChatService {

    void saveChatRoom(SaveChatRoomDto saveChatRoomDto);

    SliceResponseDto<ChatRoomDto, Object[]> getChatRoomList(long memberId, SliceRequestDto sliceRequestDto);

    void deleteChatRoom(long roomId, long memberId);

    Object[] saveChatMessage(SaveChatMessageDto saveChatMessageDto, int MemberNumInRoom);

    void enterChatRoom(long roomId, long memberId);

    SliceResponseDto<ChatMessageDto, ChatMessage> getChatMessageList(long roomId, long memberId, SliceRequestDto sliceRequestDto);
}
