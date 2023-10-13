package com.gwakkili.devbe.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyReportSaveDto {

    @JsonIgnore
    private long replyId;

    @JsonIgnore
    private long memberId;

    private Report.Type type;

    private String content;
}
