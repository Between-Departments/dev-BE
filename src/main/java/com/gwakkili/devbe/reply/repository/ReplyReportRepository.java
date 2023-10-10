package com.gwakkili.devbe.reply.repository;

import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.report.entity.ReplyReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {

    Slice<ReplyReport> findByReply(Reply reply, Pageable pageable);
}
