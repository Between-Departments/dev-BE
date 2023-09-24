package com.gwakkili.devbe.entity.report;

import com.gwakkili.devbe.entity.Reply;
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
    @JoinColumn(name = "rely_id", nullable = false)
    private Reply reply;


}
