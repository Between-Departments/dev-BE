package com.gwakkili.devbe.notification.repository;

import com.gwakkili.devbe.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByMemberMemberId(Long memberId);

    List<Notification> findAllByMemberMemberId(Long memberId);

    List<Notification> findByNotificationIdIn(List<Long> notificationIds);
}
