package com.gwakkili.devbe.report.dto.request;

import com.gwakkili.devbe.report.entity.Report;
import com.gwakkili.devbe.validation.Enum;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class PostReportSaveDto {

    @Enum(message = "Invalid Report Type!")
    private Report.Type type;

    @Length(min = 5, max = 1000)
    private String content;
}
