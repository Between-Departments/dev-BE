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

    // ! @EntityGraph로 @OneToMany가 걸린 컬렉션을 갖고올 시, 디폴트로 left join이 걸림
    @EntityGraph(attributePaths = {"images", "recommendCount", "replyCount"})
    @Query("select p from Post p join fetch p.writer w join fetch w.image where p.postId =:postId")
    Optional<Post> findWithDetailByPostId(Long postId);

    // * 내가 작성한 게시물 목록을 갖고 올 때 사용
    @EntityGraph(attributePaths = {"recommendCount", "replyCount"})
    Slice<Post> findByWriterAndBoardType(Pageable pageable, Member writer,  Post.BoardType boardType);


    // ! inner Join VS left Join
    @Query("select p, count(pr.postReportId) from Post p " +
            "join fetch p.writer w join fetch w.image inner join PostReport pr on pr.post.postId = p.postId " +
            "group by p.postId")
    Slice<Object[]> findReported(Pageable pageable);
}
