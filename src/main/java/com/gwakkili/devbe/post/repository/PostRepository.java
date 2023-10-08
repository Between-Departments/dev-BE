package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // TODO 이미지를 가져오는 쿼리가 별도로 나가는지 확인 필요
    @EntityGraph(attributePaths = {"images","writer"})
//    @Query("select p from Post p join fetch p.writer w join fetch w.image where p.postId =:postId")
    Optional<Post> findWithWriterByPostId(Long postId);

    Slice<Post> findSliceBy(Pageable pageable);

    @EntityGraph(attributePaths = {"images"})
    Slice<Post> findAllByWriter(long memberId, Pageable pageable);
}
