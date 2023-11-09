package com.gwakkili.devbe.image.repository;

import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    Optional<MemberImage> findByMember(Member member);

    @Modifying
    @Query("delete from MemberImage mi where mi.member = :member")
    void deleteByMember(Member member);
}
