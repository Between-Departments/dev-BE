package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.post.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    @EntityGraph(attributePaths = {"writer"})
    Optional<Post> findById(Long id);
}
