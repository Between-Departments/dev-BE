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
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.AccessDeniedException;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final MemberRepository memberRepository;

    @Override
    public ChatRoomDto saveChatRoom(SaveChatRoomDto saveChatRoomDto) {

        Member master = memberRepository.getReferenceById(saveChatRoomDto.getMasterId());
        System.out.println(saveChatRoomDto.getMemberId());
        Member member = memberRepository.findWithImageByMemberId(saveChatRoomDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        if (chatRoomRepository.existsByMasterAndMember(master, member))
            throw new CustomException(ExceptionCode.DUPLICATE_CHAT_ROOM);

        ChatRoom chatRoom = ChatRoom.builder()
                .master(master)
                .member(member)
                .build();
        ChatRoom saveChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomDto.of(saveChatRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getChatRoomList(long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        List<Object[]> dataList = chatRoomRepository.findWithRecentMessageByMember(member);
        if (dataList.size() == 0) throw new NotFoundException(ExceptionCode.NOT_FOUND_CHAT_ROOM);

        return dataList.stream().map(objects -> {
            ChatRoom chatRoom = (ChatRoom) objects[0];
            RecentChatMessage recentChatMessage = (RecentChatMessage) objects[1];
            int notReadCount = ((Number) objects[2]).intValue();
            boolean isMaster = chatRoom.getMaster().getMemberId() == memberId;
            return ChatRoomDto.of(chatRoom, recentChatMessage, notReadCount, isMaster);
        }).collect(Collectors.toList());
    }

    private ChatRoom getChatRoom(long roomId, long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_CHAT_ROOM));

        if (chatRoom.getMaster().getMemberId() != memberId && chatRoom.getMember().getMemberId() != memberId)
            throw new AccessDeniedException(ExceptionCode.ACCESS_DENIED);

        return chatRoom;
    }


    @Override
    public void deleteChatRoom(long roomId, long memberId) {
        ChatRoom chatRoom = getChatRoom(roomId, memberId);
        chatMessageRepository.deleteByChatRoom(chatRoom);
        chatRoomRepository.delete(chatRoom);
    }

    @Override
    public void updateChatMessageIsRead(long roomId, long memberId) {
        ChatRoom chatRoom = getChatRoom(roomId, memberId);
        Member member = memberRepository.getReferenceById(memberId);
        chatMessageRepository.updateIsReadByRoomAndMember(chatRoom, member);
    }

    @Override
    public ChatMessageDto saveChatMessage(SaveChatMessageDto saveChatMessageDto, int memberNumInRoom) {
        ChatRoom chatRoom = getChatRoom(saveChatMessageDto.getChatRoomId(), saveChatMessageDto.getSenderId());
        Member sender = memberRepository.getReferenceById(saveChatMessageDto.getSenderId());
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(saveChatMessageDto.getContent())
                .isRead(memberNumInRoom == 2)
                .build();
        ChatMessage saveChatMessage = chatMessageRepository.save(chatMessage);
        return ChatMessageDto.of(saveChatMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ChatMessageDto, ChatMessage> getChatMessageList(long roomId, long memberId, SliceRequestDto sliceRequestDto) {
        ChatRoom chatRoom = getChatRoom(roomId, memberId);
        Slice<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoom(chatRoom, sliceRequestDto.getPageable());
        if (chatMessageList.getNumberOfElements() == 0)
            throw new NotFoundException(ExceptionCode.NOT_FOUND_CHAT_MESSAGE);
        Function<ChatMessage, ChatMessageDto> fn = (ChatMessageDto::of);
        return new SliceResponseDto(chatMessageList, fn);
    }
}
