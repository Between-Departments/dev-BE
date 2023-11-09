package com.gwakkili.devbe.chat.repository;

import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByMasterAndMember(Member master, Member member);

    @Query("select cr, rcm from ChatRoom cr " +
            "join RecentChatMessage rcm on rcm.chatRoomId = cr.chatRoomId " +
            "join fetch cr.member " +
            "join fetch cr.master " +
            "join fetch cr.member.image " +
            "join fetch cr.master.image " +
            "where cr.master = :member or cr.member = :member")
    Slice<Object[]> findWithRecentMessageByMember(Member member, Pageable pageable);

    @Query("select cr from ChatRoom cr where cr.master = :member or cr.member = :member")
    List<ChatRoom> findByMember(Member member);

    @EntityGraph(attributePaths = {"master", "member"})
    @Query("select cr from ChatRoom cr where cr.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findWithMasterAndMemberById(long chatRoomId);
}
