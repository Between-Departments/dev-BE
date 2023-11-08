package com.gwakkili.devbe.member.repository;

import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Member> findByMail(String mail);

    @EntityGraph(attributePaths = "roles")
    Slice<Member> findAllByMailContaining(String mail, Pageable pageable);

    @EntityGraph(attributePaths = "image")
    @Query("select m from Member m ")
    Slice<Member> findAllWithImage(Pageable pageable);

    boolean existsByMail(String mail);

    boolean existsByNickname(String nickname);

    @EntityGraph(attributePaths = {"image"})
    Optional<Member> findWithImageByMemberId(long memberId);

    @EntityGraph(attributePaths = {"image", "posts"})
    Optional<Member> findWithImageAndPostsByMemberId(long memberId);

    @EntityGraph(attributePaths = {"bookmarkCount", "postCount", "replyCount", "image", "roles"})
    @Query("select m from Member m where m.memberId = :memberId")
    Optional<Member> findWithCountById(long memberId);

}
