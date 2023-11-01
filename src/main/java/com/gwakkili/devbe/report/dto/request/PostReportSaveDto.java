package com.gwakkili.devbe.report.dto.request;

import com.gwakkili.devbe.report.entity.Report;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class PostReportSaveDto {

    @NotNull
    private Report.Type type;

    @Length(min = 5, max = 300)
    private String content;
}
