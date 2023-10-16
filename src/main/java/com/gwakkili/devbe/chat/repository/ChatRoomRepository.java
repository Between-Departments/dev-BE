package com.gwakkili.devbe.chat.repository;

import com.gwakkili.devbe.chat.entity.ChatRoom;
import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByMasterAndMember(Member master, Member member);

    @Query("select cr from ChatRoom cr where cr.master = :member or cr.member = :member")
    Slice<ChatRoom> findByMemberOrMaster(Member member, Pageable pageable);
}
