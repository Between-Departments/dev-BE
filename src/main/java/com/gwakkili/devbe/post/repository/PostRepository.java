package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // * 단건 게시물 조회
    @EntityGraph(attributePaths = {"images", "recommendCount", "replyCount"})
    @Query("select p from Post p join fetch p.writer w join fetch w.image where p.postId =:postId")
    Optional<Post> findWithDetailByPostId(Long postId);

    // * 내가 작성한 게시물 목록
    @EntityGraph(attributePaths = {"recommendCount", "replyCount"})
    Slice<Post> findByWriterAndBoardType(Pageable pageable, Member writer,  Post.BoardType boardType);

    // * 신고된 게시물 목록
    @Query("select p, count(pr.postReportId) from Post p " +
            "join fetch p.writer w join fetch w.image " +
            "inner join PostReport pr on pr.post.postId = p.postId " +
            "group by p.postId")
    Slice<Object[]> findReported(Pageable pageable);


    // * 북마크한 게시물 목록
    @EntityGraph(attributePaths = {"recommendCount", "replyCount"})
    @Query("select p from Post p " +
            "inner join PostBookmark pb on pb.post.postId = p.postId and pb.member =:member " +
            "where p.boardType =:boardType")
    Slice<Post> findBookmarked(Pageable pageable, Member member, Post.BoardType boardType);

}
