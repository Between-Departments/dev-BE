package com.gwakkili.devbe.reply.controller;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.reply.dto.request.ReplySaveDto;
import com.gwakkili.devbe.reply.dto.request.ReplyUpdateDto;
import com.gwakkili.devbe.reply.dto.response.MyReplyDto;
import com.gwakkili.devbe.reply.dto.response.ReplyDetailDto;
import com.gwakkili.devbe.reply.dto.response.ReportedReplyDto;
import com.gwakkili.devbe.reply.entity.Reply;
import com.gwakkili.devbe.reply.service.ReplyService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@Tag(name = "Reply", description = "댓글 API")
public class ReplyController {

    private final ReplyService replyService;
//    private final SseNotificationService sseNotificationService;
//    private final StompNotificationService stompNotificationService;

    @PostMapping("/replies")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 작성")
    @ResponseStatus(HttpStatus.CREATED)
    public ReplyDetailDto saveReply(@AuthenticationPrincipal MemberDetails memberDetails,
                                    @RequestBody @Validated ReplySaveDto replySaveDto) {

        replySaveDto.setWriter(memberDetails.getMemberId());
        ReplyDetailDto replyDto = replyService.saveReply(replySaveDto);

        return replyDto;
    }


    @GetMapping("/posts/{postId}/replies")
    @Operation(summary = "댓글 목록 조회")
    public SliceResponseDto getReplyList(@Parameter(in = ParameterIn.PATH, description = "게시글 번호") @PathVariable long postId,
                                         @ParameterObject SliceRequestDto sliceRequestDto,
                                         @AuthenticationPrincipal MemberDetails memberDetails) {
        return (memberDetails == null) ? replyService.getReplyList(postId, sliceRequestDto) :
                replyService.getReplyList(postId, memberDetails.getMemberId(), sliceRequestDto);
    }

    @GetMapping("/replies/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "나의 댓글 목록 조회")
    public SliceResponseDto<MyReplyDto, Reply> getMyReplyList(@AuthenticationPrincipal MemberDetails memberDetails,
                                                              @ParameterObject SliceRequestDto sliceRequestDto) {
        return replyService.getMyReplyList(memberDetails.getMemberId(), sliceRequestDto);
    }

    @GetMapping("/replies/report")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "신고된 댓글 목록 조회 (ADMIN 전용)")
    public SliceResponseDto<ReportedReplyDto, Object[]> getReportedReplyList(@ParameterObject SliceRequestDto sliceRequestDto) {
        return replyService.getReportedReplyList(sliceRequestDto);
    }


    @PatchMapping("/replies/{replyId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 수정")
    public ReplyDetailDto UpdateReply(@Parameter(in = ParameterIn.PATH, description = "댓글 번호") @PathVariable long replyId,
                                      @AuthenticationPrincipal MemberDetails memberDetails,
                                      @RequestBody @Validated ReplyUpdateDto replyUpdateDto) {

        replyUpdateDto.setReplyId(replyId);
        replyUpdateDto.setMemberId(memberDetails.getMemberId());
        return replyService.updateReply(replyUpdateDto);
    }

    @DeleteMapping("/replies/{replyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 삭제")
    public void deleteReply(@AuthenticationPrincipal MemberDetails memberDetails,
                            @Parameter(in = ParameterIn.PATH, description = "댓글 번호") @PathVariable long replyId) {
        replyService.deleteReply(memberDetails, replyId);
    }


    @PatchMapping("/replies/{replyId}/recommend")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 추천")
    public void recommendReply(@AuthenticationPrincipal MemberDetails memberDetails,
                               @Parameter(in = ParameterIn.PATH, description = "댓글 번호") @PathVariable long replyId) {
        replyService.recommendReply(memberDetails.getMemberId(), replyId);
    }


}
