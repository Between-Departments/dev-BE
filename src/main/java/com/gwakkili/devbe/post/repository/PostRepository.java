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

    // TODO 이미지를 가져오는 쿼리가 별도로 나가는지 확인 필요
    // ! Member에서 MemberImage를 LAZY로하던 EAGER로 하던 상관없이 SQL에서 안갖고옴.
    // ! @EntityGraph로 @OneToMany가 걸린 컬렉션을 갖고올 시, 디폴트로 left join이 걸림
    @EntityGraph(attributePaths = {"images", "recommendCount"})
    @Query("select p from Post p join fetch p.writer w join fetch w.image where p.postId =:postId")
    Optional<Post> findWithDetailByPostId(Long postId);


    // * 검색 조건에 맞게 Post를 갖고올 떄 쓰는 함수
    Slice<Post> findSliceBy(Pageable pageable);

    // * 내가 작성한 게시물 목록을 갖고 올 때 사용
    @EntityGraph(attributePaths = {"images"})
    Slice<Post> findByWriter(Member writer, Pageable pageable);


    // ! inner Join VS left Join
    @Query("select p, count(pr.postReportId) from Post p " +
            "join fetch p.writer w join fetch w.image inner join PostReport pr on pr.post.postId = p.postId " +
            "group by p.postId")
    Slice<Object[]> findReported(Pageable pageable);
}
