package com.gwakkili.devbe.report.dto.request;

import com.gwakkili.devbe.report.entity.Report;
import lombok.Getter;

@Getter
public class PostReportSaveDto {

    private Report.Type type;
    private String content;
}
