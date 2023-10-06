package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"images"})
    @Query("select p from Post p join fetch p.writer w join fetch w.image where p.postId =:postId")
    Optional<Post> findWithWriterByPostId(Long postId);

    Slice<Post> findSliceBy(Pageable pageable);

    Slice<Post> findAllByWriter(long memberId, Pageable pageable);
}
