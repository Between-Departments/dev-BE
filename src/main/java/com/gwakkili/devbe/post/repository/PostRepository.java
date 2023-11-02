package com.gwakkili.devbe.post.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"images"})
    Optional<Post> findWithImagesByPostId(long postId);

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

    @EntityGraph(attributePaths = {"recommendCount", "replyCount"})
    @Query("select p from Post p join fetch p.writer w join fetch w.image where p.createAt >= :start and p.createAt <= :end order by p.recommendCount desc limit 5")
    List<Post> findWeeklyHot(LocalDateTime start, LocalDateTime end);

    @Query(value = "select post_id, title, major_category, create_at from " +
            "(select *, ROW_NUMBER() over (partition by tmp1.major_category order by tmp1.recommendCount desc) as rnk " +
            "from (select *, (select count(1) from post_recommend pr where pr.post_id = p.post_id) as recommendCount from post p where p.board_type = 'NEED_HELP' and p.create_at >= ? and p.create_at <= ?) as tmp1) as tmp2 " +
            "where tmp2.rnk = 1", nativeQuery = true)
    List<Object[]> findDailyHot(LocalDateTime start, LocalDateTime end);

    List<Post> findByWriter(Member writer);

}
