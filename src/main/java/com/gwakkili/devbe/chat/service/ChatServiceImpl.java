package com.gwakkili.devbe.chat.service;

import com.gwakkili.devbe.chat.dto.Request.SaveChatMessageDto;
import com.gwakkili.devbe.chat.dto.Request.SaveChatRoomDto;
import com.gwakkili.devbe.chat.dto.Response.ChatMessageDto;
import com.gwakkili.devbe.chat.dto.Response.ChatRoomDto;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.chat.entity.RecentChatMessage;
import com.gwakkili.devbe.chat.repository.ChatMessageRepository;
import com.gwakkili.devbe.chat.repository.ChatRoomRepository;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.event.NewChatMessageEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher publisher;


    @Override
    public long saveChatRoom(SaveChatRoomDto saveChatRoomDto) {

        Member master = memberRepository.getReferenceById(saveChatRoomDto.getMasterId());
        Member member = memberRepository.findById(saveChatRoomDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        Optional<ChatRoom> optional = chatRoomRepository.findByMasterAndMember(master, member);
        if (optional.isPresent()) return optional.get().getChatRoomId();

        ChatRoom chatRoom = ChatRoom.builder()
                .master(master)
                .member(member)
                .build();

        chatRoomRepository.save(chatRoom);
        return chatRoom.getChatRoomId();
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ChatRoomDto, Object[]> getChatRoomList(long memberId, SliceRequestDto sliceRequestDto) {

        Member member = memberRepository.getReferenceById(memberId);
        Slice<Object[]> dataSlice = chatRoomRepository.findWithRecentMessageByMember(member, sliceRequestDto.getPageable());
        
        Function<Object[], ChatRoomDto> fn = (objects -> {
            ChatRoom chatRoom = (ChatRoom) objects[0];
            RecentChatMessage recentChatMessage = (RecentChatMessage) objects[1];
            return ChatRoomDto.of(chatRoom, recentChatMessage, memberId);
        });

        return new SliceResponseDto(dataSlice, fn);
    }

    private ChatRoom getChatRoom(long roomId, long memberId) {

        ChatRoom chatRoom = chatRoomRepository.findWithMasterAndMemberById(roomId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_CHAT_ROOM));

        if (chatRoom.getMaster().getMemberId() != memberId && chatRoom.getMember().getMemberId() != memberId)
            throw new AccessDeniedException("요청 거부");

        return chatRoom;
    }


    @Override
    public void deleteChatRoom(long roomId, long memberId) {

        ChatRoom chatRoom = getChatRoom(roomId, memberId);
        chatRoomRepository.delete(chatRoom);
    }


    @Override
    public void enterChatRoom(long roomId, long memberId) {

        ChatRoom chatRoom = getChatRoom(roomId, memberId);
        Member member = memberRepository.getReferenceById(memberId);
        chatMessageRepository.updateIsReadByRoomAndMember(chatRoom, member);
    }

    @Override
    public ChatMessageDto saveChatMessage(SaveChatMessageDto saveChatMessageDto, int memberNumInRoom) {

        ChatRoom chatRoom = getChatRoom(saveChatMessageDto.getChatRoomId(), saveChatMessageDto.getSenderId());
        Member sender = memberRepository.findById(saveChatMessageDto.getSenderId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(saveChatMessageDto.getContent())
                .isRead(memberNumInRoom == 2)
                .build();

        String receiver = (sender.getMail().equals(chatRoom.getMaster().getMail())) ?
                chatRoom.getMember().getMail() : chatRoom.getMaster().getMail();

        publisher.publishEvent(new NewChatMessageEvent(receiver, chatMessage));
        ChatMessage saveChatMessage = chatMessageRepository.save(chatMessage);

        return ChatMessageDto.of(saveChatMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ChatMessageDto, ChatMessage> getChatMessageList(long roomId, long memberId, SliceRequestDto sliceRequestDto) {

        ChatRoom chatRoom = getChatRoom(roomId, memberId);
        Slice<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoom(chatRoom, sliceRequestDto.getPageable());
        Function<ChatMessage, ChatMessageDto> fn = (ChatMessageDto::of);

        return new SliceResponseDto(chatMessageList, fn);
    }
}
