package com.gwakkili.devbe.report.controller;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.report.dto.request.PostReportSaveDto;
import com.gwakkili.devbe.report.dto.request.ReplyReportSaveDto;
import com.gwakkili.devbe.report.dto.response.PostReportDto;
import com.gwakkili.devbe.report.dto.response.ReplyReportDto;
import com.gwakkili.devbe.report.entity.PostReport;
import com.gwakkili.devbe.report.entity.ReplyReport;
import com.gwakkili.devbe.report.service.PostReportService;
import com.gwakkili.devbe.report.service.ReplyReportService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Report", description = "신고 API")
public class ReportController {

    private final PostReportService postReportService;
    private final ReplyReportService replyReportService;

    @Operation(summary = "게시물 신고 목록 조회 (ADMIN 전용)")
    @GetMapping("/posts/{postId}/report")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public SliceResponseDto<PostReportDto, PostReport> getPostReportList(@PathVariable long postId,
                                                                         @ParameterObject SliceRequestDto sliceRequestDto) {
        return postReportService.findPostReportList(postId, sliceRequestDto);
    }

    @Operation(method = "POST", summary = "특정 게시글 신고")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 게시글 신고 성공")
    })
    @PostMapping("/posts/{postId}/report")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void savePostReport(@PathVariable Long postId,
                               @RequestBody @Valid PostReportSaveDto postReportSaveDto,
                               @AuthenticationPrincipal MemberDetails memberDetails){
        postReportService.saveNewPostReport(postReportSaveDto, postId, memberDetails.getMemberId());
    }

    @DeleteMapping("/posts/{postId}/report/{reportId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deletePostReport(@PathVariable long postId, @PathVariable long reportId) {
        postReportService.deletePostReport(reportId);
    }

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
    @Operation(summary = "댓글 신고")
    public void saveReplyReport(@Parameter(in = ParameterIn.PATH, description = "댓글 번호") @PathVariable long replyId,
                                @AuthenticationPrincipal MemberDetails memberDetails,
                                @RequestBody @Valid ReplyReportSaveDto replyReportSaveDto) {
        replyReportSaveDto.setReplyId(replyId);
        replyReportSaveDto.setMemberId(memberDetails.getMemberId());
        replyReportService.saveReplyReport(replyReportSaveDto);
    }

    @DeleteMapping("/replies/{replyId}/report/{reportId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "댓글 신고 삭제")
    public void deleteReplyReport(@Parameter(in = ParameterIn.PATH, description = "댓글 번호") @PathVariable long replyId,
                                  @Parameter(in = ParameterIn.PATH, description = "댓글 신고 번호") @PathVariable long reportId) {
        replyReportService.deleteReplyReport(reportId);
    }
}
