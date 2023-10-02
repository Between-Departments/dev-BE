package com.gwakkili.devbe.member.repository;

import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMail(String mail);

    Slice<Member> findAllByMailContaining(String mail, Pageable pageable);

    boolean existsByMail(String mail);

    boolean existsByNickname(String nickname);
}
