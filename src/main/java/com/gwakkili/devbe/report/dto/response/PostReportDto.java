package com.gwakkili.devbe.report.dto.response;

import com.gwakkili.devbe.report.entity.PostReport;
import com.gwakkili.devbe.report.entity.Report;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostReportDto {

    private long postId;

    private String reporter;

    private Report.Type type;

    private String content;
    public static PostReportDto of(PostReport postReport) {
        return PostReportDto.builder()
                // ! 이러면 postId가 N개 중복으로 나감
                .postId(postReport.getPost().getPostId())
                .reporter(postReport.getReporter().getNickname())
                .type(postReport.getType())
                .content(postReport.getContent())
                .build();
    }
}
