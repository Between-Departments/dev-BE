package com.gwakkili.devbe.reply.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.reply.entity.Reply;
import jakarta.persistence.Entity;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Slice<Reply> findByPost(Post post, Pageable pageable);

    Slice<Reply> findByMember(Member member, Pageable pageable);

    @Query("select r ,count(rr) from Reply r join fetch r.member inner join ReplyReport rr on rr.reply = r group by r")
    Slice<Object[]> findReported(Pageable pageable);
}
