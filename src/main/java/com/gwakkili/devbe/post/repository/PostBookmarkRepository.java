package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.post.entity.PostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    Optional<PostBookmark> findByMemberAndPost(Member member, Post post);

    Boolean existsByMemberAndPost(Member member, Post post);

    @Modifying
    @Query("delete from PostBookmark pb where pb.member = :member")
    void deleteByMember(Member member);

    @Modifying
    @Query("delete from PostBookmark pb where pb.post in :postList")
    void deleteByPostIn(List<Post> postList);
}
