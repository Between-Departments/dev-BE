package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    Optional<PostBookmark> findByMemberAndPost(Member member, Post post);
}
