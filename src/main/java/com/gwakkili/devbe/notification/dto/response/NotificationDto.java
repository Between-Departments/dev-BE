package com.gwakkili.devbe.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotificationDto {

    private Long notificationId;

    private Notification.Type type;

    private String content;

    private Long postId;

    private Long replyId;

    private LocalDateTime createAt;

    public static NotificationDto of(Notification notification){
        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .type(notification.getType())
                .content(notification.getContent())
                .postId(notification.getPostId())
                .replyId(notification.getReplyId())
                .createAt(notification.getCreateAt())
                .build();
    }

}
