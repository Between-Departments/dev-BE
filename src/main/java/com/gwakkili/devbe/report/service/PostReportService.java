package com.gwakkili.devbe.report.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.report.dto.response.PostReportDto;
import com.gwakkili.devbe.report.dto.request.PostReportSaveDto;
import com.gwakkili.devbe.report.entity.PostReport;

public interface PostReportService {
    void saveNewPostReport(PostReportSaveDto postReportSaveDto, Long postId, long memberId);

    void deletePostReport(long reportId);

    SliceResponseDto<PostReportDto, PostReport> findPostReportList(long postId, SliceRequestDto sliceRequestDto);
}