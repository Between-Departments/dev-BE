package com.gwakkili.devbe.entity.report;

import com.gwakkili.devbe.entity.Member;
import com.gwakkili.devbe.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(value = EnumType.STRING)
    private ReportType reportType;

    private String content;
}
