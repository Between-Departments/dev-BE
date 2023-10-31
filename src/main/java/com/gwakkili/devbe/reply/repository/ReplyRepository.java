package com.gwakkili.devbe.reply.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.reply.entity.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @EntityGraph(attributePaths = "recommendCount")
    @Query("select r, case when rr is null then false else true end " +
            "from Reply r join fetch r.member m join fetch m.image " +
            "left join ReplyRecommend rr on rr.reply = r and r.member = :member where r.post = :post")
    Slice<Object[]> findWithRecommendByPostAndMember(Post post, Member member, Pageable pageable);

    @EntityGraph(attributePaths = "recommendCount")
    @Query("select r, case when rr is null then false else true end " +
            "from Reply r join fetch r.member m join fetch m.image " +
            "left join ReplyRecommend rr on rr.reply = r and r.member = :member where r.replyId = :replyId")
    Optional<Object[]> findWithRecommendByIdAndMember(long replyId, Member member);

    @EntityGraph(attributePaths = "recommendCount")
    @Query("select r from Reply r join fetch r.member m join fetch m.image where r.post = :post")
    Slice<Reply> findByPost(Post post, Pageable pageable);

    @EntityGraph(attributePaths = "recommendCount")
    @Query("select r from Reply r join fetch r.member m join fetch m.image where r.member = :member")
    Slice<Reply> findByMember(Member member, Pageable pageable);

    @Query("select r ,count(rr) from Reply r join fetch r.member m join fetch m.image " +
            "inner join ReplyReport rr on rr.reply = r group by r")
    Slice<Object[]> findReported(Pageable pageable);

    List<Reply> findByMember(Member member);

    List<Reply> findByPostIn(List<Post> postList);
}
