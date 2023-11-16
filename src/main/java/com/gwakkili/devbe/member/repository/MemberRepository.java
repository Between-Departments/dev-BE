package com.gwakkili.devbe.member.repository;

import com.gwakkili.devbe.member.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // TODO 비관적 락에 의해 성능이 많이 떨어질수도 있겠다는 생각이 듦
    @Query("select m from Member m where m.memberId = :memberId")
    Optional<Member> findByIdForUpdate(Long memberId);

    @EntityGraph(attributePaths = "roles")
    Optional<Member> findByMail(String mail);

    @EntityGraph(attributePaths = {"roles", "image"})
    @Query("select m from Member m where m.mail like concat('%', :keyword, '%') or m.nickname like concat('%', :keyword, '%')")
    Slice<Member> findAllByKeyword(String keyword, Pageable pageable);

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
