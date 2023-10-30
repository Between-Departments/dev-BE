package com.gwakkili.devbe.reply.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.entity.ReplyRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReplyRecommendRepository extends JpaRepository<ReplyRecommend, Long> {

    Optional<ReplyRecommend> findByMemberAndReply(Member member, Reply reply);

    @Modifying
    @Query("delete from ReplyRecommend rr where rr.member = :member")
    void deleteByMember(Member member);

    @Modifying
    @Query("delete from ReplyRecommend rr where rr.reply in :replyList")
    void deleteByReplyIn(List<Reply> replyList);
}
