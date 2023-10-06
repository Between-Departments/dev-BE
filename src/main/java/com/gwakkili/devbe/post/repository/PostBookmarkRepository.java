package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    Optional<PostBookmark> findByMemberAndPost(Member member, Post post);

    @Query("select pb from PostBookmark pb " +
            "join fetch pb.post p join fetch p.images join fetch p.writer w join fetch w.image " +
            "where pb.member.memberId =:memberId")
    Slice<PostBookmark> findAllByMemberId(long memberId, Pageable pageable);
}
