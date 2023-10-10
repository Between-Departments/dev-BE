package com.gwakkili.devbe.member.repository;

import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMail(String mail);

    Slice<Member> findAllByMailContaining(String mail, Pageable pageable);

    boolean existsByMail(String mail);

    boolean existsByNickname(String nickname);


    @EntityGraph(attributePaths = {"image"})
    Optional<Member> findWithImageByMemberId(long memberId);

    @EntityGraph(attributePaths = {"bookmarkCount", "postCount", "replyCount", "image"})
    @Query("select m from Member m where m.memberId = :memberId")
    Optional<Member> findWithCountById(@Param("memberId") long memberId);

}
