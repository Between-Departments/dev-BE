package com.gwakkili.devbe.image.repository;

import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Modifying
    @Query("delete from PostImage pi where pi.post.postId = :postId")
    void deleteByPostId(long postId);

    List<PostImage> findByPostIn(List<Post> postList);
}
