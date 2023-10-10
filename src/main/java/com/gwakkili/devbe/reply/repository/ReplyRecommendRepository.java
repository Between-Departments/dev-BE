package com.gwakkili.devbe.reply.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.entity.ReplyRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRecommendRepository extends JpaRepository<ReplyRecommend, Long> {

    Optional<ReplyRecommend> findByMemberAndReply(Member member, Reply reply);
}
