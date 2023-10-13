package com.gwakkili.devbe.report.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.report.dto.ReplyReportDto;
import com.gwakkili.devbe.report.dto.ReplyReportSaveDto;
import com.gwakkili.devbe.report.entity.ReplyReport;

public interface ReplyReportService {

    SliceResponseDto<ReplyReportDto, ReplyReport> getReplyReportList(long replyId, SliceRequestDto sliceRequestDto);

    void saveReplyReport(ReplyReportSaveDto replyReportSaveDto);

    void deleteReplyReport(long reportId);
}
