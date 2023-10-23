package com.gwakkili.devbe.reply.controller;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.notification.sse.NotificationService;
import com.gwakkili.devbe.notification.event.NewReplyEvent;
import com.gwakkili.devbe.reply.dto.*;
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
    private final NotificationService notificationService;

    @PostMapping("/replies")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 작성")
    @ResponseStatus(HttpStatus.CREATED)
    public ReplyDto saveReply(@AuthenticationPrincipal MemberDetails memberDetails,
                              @RequestBody @Validated ReplySaveDto replySaveDto) {

        replySaveDto.setWriter(memberDetails.getMemberId());
        ReplyDto replyDto = replyService.saveReply(replySaveDto);

        if (!replyDto.isAnonymous()) {
            notificationService.notify(replyDto.getPostWriterId(), new NewReplyEvent(replyDto.getContent()));
        }

        return replyDto;
    }


    @GetMapping("/posts/{postId}/replies")
    @Operation(summary = "댓글 목록 조회")
    public SliceResponseDto<ReplyDto, Reply> getReplyList(@Parameter(in = ParameterIn.PATH, description = "게시글 번호") @PathVariable long postId,
                                                          @ParameterObject SliceRequestDto sliceRequestDto) {
        return replyService.getReplyList(postId, sliceRequestDto);
    }

    @GetMapping("/replies/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "나의 댓글 목록 조회")
    public SliceResponseDto<ReplyDto, Reply> getReplyList(@AuthenticationPrincipal MemberDetails memberDetails,
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
    public ReplyDto UpdateReply(@Parameter(in = ParameterIn.PATH, description = "댓글 번호") @PathVariable long replyId,
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
