package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.report.entity.Report;
import lombok.Getter;

@Getter
public class PostReportDto {

    private Report.Type type;
    private String content;
}
