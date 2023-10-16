package com.gwakkili.devbe.chat.service;

import com.gwakkili.devbe.chat.dto.Request.SaveChatMessageDto;
import com.gwakkili.devbe.chat.dto.Request.SaveChatRoomDto;
import com.gwakkili.devbe.chat.dto.Response.ChatMessageDto;
import com.gwakkili.devbe.chat.dto.Response.ChatRoomDto;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.chat.repository.ChatMessageRepository;
import com.gwakkili.devbe.chat.repository.ChatRoomRepository;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final MemberRepository memberRepository;

    @Override
    public ChatRoomDto saveChatRoom(SaveChatRoomDto saveChatRoomDto) {

        Member master = memberRepository.getReferenceById(saveChatRoomDto.getMaster());
        Member member = memberRepository.getReferenceById(saveChatRoomDto.getMember());

        if (chatRoomRepository.existsByMasterAndMember(master, member)) return null;

        ChatRoom chatRoom = ChatRoom.builder()
                .master(master)
                .member(member)
                .build();
        ChatRoom saveChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomDto.of(saveChatRoom);
    }

    @Override
    public SliceResponseDto<ChatRoomDto, ChatRoom> getChatRoomList(long memberId) {
        return null;
    }

    @Override
    public void deleteChatRoom(long chatRoomId) {

    }

    @Override
    public ChatMessageDto saveChatMessage(SaveChatMessageDto saveChatMessageDto) {
        return null;
    }

    @Override
    public SliceResponseDto<ChatMessageDto, ChatMessage> getChatMessageList(long chatRoomId) {
        return null;
    }
}
