package com.gwakkili.devbe.notification.repository;

import com.gwakkili.devbe.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMemberMemberId(Long memberId);
}
