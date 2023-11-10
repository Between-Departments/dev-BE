package com.gwakkili.devbe.report.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.report.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema
public class ReplyReportSaveDto {

    @JsonIgnore
    private long replyId;

    @JsonIgnore
    private long memberId;

    @NotNull
    @Schema(description = "신고 유형")
    private Report.Type type;

    @Length(min = 5, max = 300)
    @Schema(description = "신고 내용", example = "경쟁률이 어쩌구 저쩌구")
    @NotBlank
    private String content;

    @Builder
    public ReplyReportSaveDto(Report.Type type, String content) {
        this.type = type;
        this.content = content;
    }
}
