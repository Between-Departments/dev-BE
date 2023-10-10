package com.gwakkili.devbe.reply.dto;

import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.report.entity.Report;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReplyReportDto {

    private long replyId;

    private String reporter;

    private Report.Type type;

    private String content;

    public static ReplyReportDto of(ReplyReport replyReport) {
        return ReplyReportDto.builder()
                .replyId(replyReport.getReply().getReplyId())
                .reporter(replyReport.getReporter().getNickname())
                .type(replyReport.getType())
                .content(replyReport.getContent())
                .build();
    }
}
