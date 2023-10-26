package com.gwakkili.devbe.chat.repository;

import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByMasterAndMember(Member master, Member member);

    @Query("select cr, rcm, (select count(*) from ChatMessage cm where cm.chatRoom = cr and cm.sender != :member and cm.isRead = false) from ChatRoom cr " +
            "join fetch cr.member " +
            "left join RecentChatMessage rcm on rcm.chatRoomId = cr.chatRoomId " +
            "join fetch cr.master " +
            "join fetch cr.member.image " +
            "join fetch cr.master.image " +
            "where cr.master = :member or cr.member = :member")
    List<Object[]> findWithRecentMessageByMember(Member member);

}
