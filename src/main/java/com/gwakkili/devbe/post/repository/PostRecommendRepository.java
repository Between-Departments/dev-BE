package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {
    Optional<PostRecommend> findByMemberAndPost(Member member, Post post);

    Boolean existsByMemberAndPost(Member member, Post post);

}
