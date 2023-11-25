package com.gwakkili.devbe.notification.controller;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.notification.dto.request.NotificationDeleteRequest;
import com.gwakkili.devbe.notification.dto.response.NotificationDto;
import com.gwakkili.devbe.notification.entity.Notification;
import com.gwakkili.devbe.notification.service.NotificationService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "알림", description = "알림 API")
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(method = "GET", summary = "새로운 알림 여부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "새로운 알림 여부 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public Boolean getNewNotification(@AuthenticationPrincipal MemberDetails memberDetails){
        return notificationService.findAnyNotification(memberDetails.getMemberId());
    }

    @Operation(method = "GET", summary = "알림 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ListResponseDto<NotificationDto, Notification> getAll(@AuthenticationPrincipal MemberDetails memberDetails){
        return notificationService.findAllNotifications(memberDetails.getMemberId());
    }

    @Operation(method = "DELETE", summary = "알림 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공")
    })
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long notificationId, @AuthenticationPrincipal MemberDetails memberDetails){
        notificationService.deleteNotification(notificationId, memberDetails.getMemberId());
    }


    // TODO 본인의 알림 데이터가 맞는지 확인하는 로직 필요
    @Operation(method = "DELETE", summary = "알림 여러 개 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 여러 개 삭제 성공")
    })
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody NotificationDeleteRequest notificationDeleteRequest, @AuthenticationPrincipal MemberDetails memberDetails){
        notificationService.deleteNotifications(notificationDeleteRequest.getNotificationIds(), memberDetails.getMemberId());
    }
}
