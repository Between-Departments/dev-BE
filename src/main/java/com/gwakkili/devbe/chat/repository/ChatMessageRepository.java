package com.gwakkili.devbe.chat.repository;

import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Slice<ChatMessage> findByChatRoom(ChatRoom chatRoom, Pageable pageable);

    @Modifying
    @Query("update ChatMessage cm set cm.isRead = true where cm.chatRoom = :chatRoom and cm.sender != :member")
    void updateIsReadByRoomAndMember(ChatRoom chatRoom, Member member);

    @Modifying
    @Query("delete from ChatMessage cm where cm.chatRoom = :chatRoom")
    void deleteByChatRoom(ChatRoom chatRoom);

    @Modifying
    @Query("delete from ChatMessage cm where cm.chatRoom in :chatRoomList")
    void deleteByChatRoomIn(List<ChatRoom> chatRoomList);

}
