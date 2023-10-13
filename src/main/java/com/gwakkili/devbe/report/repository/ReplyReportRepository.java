package com.gwakkili.devbe.report.repository;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.report.entity.ReplyReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {

    @EntityGraph("reporter")
    Slice<ReplyReport> findByReply(Reply reply, Pageable pageable);

    boolean existsByReporterAndReply(Member reporter, Reply reply);

}
