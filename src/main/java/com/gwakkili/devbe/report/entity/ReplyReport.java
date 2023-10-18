package com.gwakkili.devbe.report.entity;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.reply.entity.Reply;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyReport extends Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;

    @Builder
    public ReplyReport(Member reporter, Type type, String content, Reply reply) {
        super(reporter, type, content);
        this.reply = reply;
    }
  
}
