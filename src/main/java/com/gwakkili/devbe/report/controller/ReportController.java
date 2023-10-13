package com.gwakkili.devbe.report.controller;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.report.dto.ReplyReportDto;
import com.gwakkili.devbe.report.dto.ReplyReportSaveDto;
import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.report.service.ReplyReportService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReplyReportService replyReportService;

    @GetMapping("/replies/{replyId}/report")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "댓글 신고 목록 조회 (ADMIN 전용)")
    public SliceResponseDto<ReplyReportDto, ReplyReport> getReplyReportList(@Parameter(in = ParameterIn.PATH, description = "댓글 번호")
                                                                            @PathVariable long replyId,
                                                                            @ParameterObject SliceRequestDto sliceRequestDto) {
        return replyReportService.getReplyReportList(replyId, sliceRequestDto);
    }

    @PostMapping("/replies/{replyId}/report")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void saveReplyReport(@Parameter(in = ParameterIn.PATH, description = "댓글 번호") @PathVariable long replyId,
                                @AuthenticationPrincipal MemberDetails memberDetails,
                                @RequestBody ReplyReportSaveDto replyReportSaveDto) {
        replyReportSaveDto.setReplyId(replyId);
        replyReportSaveDto.setMemberId(memberDetails.getMemberId());
        replyReportService.saveReplyReport(replyReportSaveDto);
    }

    @DeleteMapping("/replies/{replyId}/report/{reportId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteReplyReport(@PathVariable long replyId, @PathVariable long reportId) {
        replyReportService.deleteReplyReport(reportId);
    }
}
