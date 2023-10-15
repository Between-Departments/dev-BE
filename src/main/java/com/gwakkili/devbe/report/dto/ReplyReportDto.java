package com.gwakkili.devbe.report.dto;

import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.report.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class ReplyReportDto {

    @Schema(description = "댓글 신고 번호")
    private long replyReportId;

    @Schema(description = "댓글 번호")
    private long replyId;

    @Schema(description = "신고자", example = "고슴도치사냥꾼")
    private String reporter;

    @Schema(description = "신고 유형")
    private Report.Type type;

    @Schema(description = "신고 내용", example = "경쟁률이 어쩌구 저쩌구")
    private String content;

    public static ReplyReportDto of(ReplyReport replyReport) {
        return ReplyReportDto.builder()
                .replyReportId(replyReport.getReplyReportId())
                .replyId(replyReport.getReply().getReplyId())
                .reporter(replyReport.getReporter().getNickname())
                .type(replyReport.getType())
                .content(replyReport.getContent())
                .build();
    }
}
