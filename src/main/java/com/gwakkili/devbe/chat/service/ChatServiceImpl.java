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
import com.gwakkili.devbe.event.DeleteMemberEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.DuplicateException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

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
        Member member = memberRepository.findWithImageByMemberId(saveChatRoomDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        if (chatRoomRepository.existsByMasterAndMember(master, member))
            throw new DuplicateException(ExceptionCode.DUPLICATE_CHAT_ROOM);

        ChatRoom chatRoom = ChatRoom.builder()
                .master(master)
                .member(member)
                .build();
        ChatRoom saveChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomDto.of(saveChatRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<ChatRoomDto, Object[]> getChatRoomList(long memberId, SliceRequestDto sliceRequestDto) {
        Member member = memberRepository.getReferenceById(memberId);
        Slice<Object[]> dataList = chatRoomRepository.findWithRecentMessageByMember(member, sliceRequestDto.getPageable());
        if (dataList.getNumberOfElements() == 0) throw new NotFoundException(ExceptionCode.NOT_FOUND_CHAT_ROOM);
        Function<Object[], ChatRoomDto> fn = (objects -> {
            ChatRoom chatRoom = (ChatRoom) objects[0];
            RecentChatMessage recentChatMessage = (RecentChatMessage) objects[1];
            boolean isMaster = chatRoom.getMaster().getMemberId() == memberId;
            return ChatRoomDto.of(chatRoom, recentChatMessage, isMaster);
        });
        return new SliceResponseDto(dataList, fn);
    }

    private ChatRoom getChatRoom(long roomId, long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_CHAT_ROOM));

        if (chatRoom.getMaster().getMemberId() != memberId && chatRoom.getMember().getMemberId() != memberId)
            throw new AccessDeniedException("요청 거부");

        return chatRoom;
    }


    @Override
    public void deleteChatRoom(long roomId, long memberId) {
        ChatRoom chatRoom = getChatRoom(roomId, memberId);
        chatMessageRepository.deleteByChatRoom(chatRoom);
        chatRoomRepository.delete(chatRoom);
    }

    @EventListener
    public void deleteChatRoom(DeleteMemberEvent deleteMemberEvent) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByMember(deleteMemberEvent.getMember());
        // 해당 회원이 속한 채팅방의 채팅메시지 삭제
        chatMessageRepository.deleteByChatRoomIn(chatRoomList);
        // 해당 회원이 속한 채팅방 삭제
        chatRoomRepository.deleteAllInBatch(chatRoomList);
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
        Function<ChatMessage, ChatMessageDto> fn = (ChatMessageDto::of);
        return new SliceResponseDto(chatMessageList, fn);
    }
}
