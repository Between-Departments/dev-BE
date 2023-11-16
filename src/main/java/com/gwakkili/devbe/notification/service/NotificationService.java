package com.gwakkili.devbe.notification.service;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.notification.dto.response.NotificationDto;
import com.gwakkili.devbe.notification.entity.Notification;
import com.gwakkili.devbe.event.NewPostReportEvent;
import com.gwakkili.devbe.event.NewReplyEvent;
import com.gwakkili.devbe.event.NewReplyReportEvent;

import java.util.List;

public interface NotificationService {
    ListResponseDto<NotificationDto, Notification> findAllNotifications(Long memberId);

    void saveNewNotification(NewReplyEvent newReplyEvent);

    void saveNewNotification(NewPostReportEvent newPostReportEvent);

    void saveNewNotification(NewReplyReportEvent newReplyReportEvent);

    void deleteNotification(Long notificationId, Long memberId);

    void deleteNotifications(List<Long> notificationIds, Long memberId);
}

