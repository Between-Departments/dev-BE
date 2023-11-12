package com.gwakkili.devbe.report.entity;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.reply.entity.Reply;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reply_report",
        uniqueConstraints = @UniqueConstraint(name = "unique_member_and_reply", columnNames = {"member_id", "reply_id"})
)
public class ReplyReport extends Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reply reply;

    @Builder
    public ReplyReport(Member reporter, Type type, String content, Reply reply) {
        super(reporter, type, content);
        this.reply = reply;
    }
  
}
