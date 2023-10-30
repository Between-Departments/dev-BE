package com.gwakkili.devbe.image.repository;

import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    Optional<MemberImage> findByMember(Member member);
}
