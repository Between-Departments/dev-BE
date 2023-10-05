package com.gwakkili.devbe.report.entity;

import com.gwakkili.devbe.reply.entity.Reply;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReplyReport extends Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyReportId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;

}
