package com.gwakkili.devbe.chat.service;

import com.gwakkili.devbe.chat.dto.Request.SaveChatMessageDto;
import com.gwakkili.devbe.chat.dto.Request.SaveChatRoomDto;
import com.gwakkili.devbe.chat.dto.Response.ChatMessageDto;
import com.gwakkili.devbe.chat.dto.Response.ChatRoomDto;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.dto.SliceResponseDto;

public interface ChatService {

    ChatRoomDto saveChatRoom(SaveChatRoomDto saveChatRoomDto);

    SliceResponseDto<ChatRoomDto, ChatRoom> getChatRoomList(long memberId);

    void deleteChatRoom(long chatRoomId);

    ChatMessageDto saveChatMessage(SaveChatMessageDto saveChatMessageDto);

    SliceResponseDto<ChatMessageDto, ChatMessage> getChatMessageList(long chatRoomId);
}
