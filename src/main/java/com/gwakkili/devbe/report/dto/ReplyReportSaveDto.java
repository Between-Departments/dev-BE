package com.gwakkili.devbe.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.report.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class ReplyReportSaveDto {

    @JsonIgnore
    private long replyId;

    @JsonIgnore
    private long memberId;

    @Schema(description = "신고 유형")
    private Report.Type type;

    @Schema(description = "신고 내용", example = "경쟁률이 어쩌구 저쩌구")
    @NotBlank
    private String content;
}
