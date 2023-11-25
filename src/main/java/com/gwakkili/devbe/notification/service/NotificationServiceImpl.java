package com.gwakkili.devbe.notification.service;

import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.event.NewChatMessageEvent;
import com.gwakkili.devbe.event.NewPostReportEvent;
import com.gwakkili.devbe.event.NewReplyEvent;
import com.gwakkili.devbe.event.NewReplyReportEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.notification.dto.response.ChatNotificationDto;
import com.gwakkili.devbe.notification.dto.response.NotificationDto;
import com.gwakkili.devbe.notification.entity.Notification;
import com.gwakkili.devbe.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    private void sendNotification(Notification notification) {
        NotificationDto notificationDto = NotificationDto.of(notification);
        String mail = notification.getMember().getMail();
        messagingTemplate.convertAndSendToUser(mail, "/sub/notifications", notificationDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendChatNotification(NewChatMessageEvent newChatMessageEvent) {

        String mail = newChatMessageEvent.getMail();
        ChatMessage chatMessage = newChatMessageEvent.getChatMessage();
        messagingTemplate.convertAndSendToUser(mail, "/sub/notifications", ChatNotificationDto.of(chatMessage));
    }


    // TODO ? 이벤트리스너용 함수를 Interface로 빼는게 괜찮은가?
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveNewNotification(NewReplyEvent newReplyEvent) {

        Notification newNotification = Notification.builder()
                .member(newReplyEvent.getMember())
                .content(newReplyEvent.getContent())
                .type(Notification.Type.REPLY)
                .postId(newReplyEvent.getPostId())
                .replyId(newReplyEvent.getReplyId())
                .build();

        notificationRepository.save(newNotification);
        sendNotification(newNotification);
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveNewNotification(NewPostReportEvent newPostReportEvent) {
        Notification newNotification = Notification.builder()
                .member(newPostReportEvent.getMember())
                .content(newPostReportEvent.getContent())
                .type(Notification.Type.POST_REPORT)
                .postId(newPostReportEvent.getPostId())
                .replyId(newPostReportEvent.getReplyId())
                .build();

        notificationRepository.save(newNotification);
        sendNotification(newNotification);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveNewNotification(NewReplyReportEvent newReplyReportEvent) {
        Notification newNotification = Notification.builder()
                .member(newReplyReportEvent.getMember())
                .content(newReplyReportEvent.getContent())
                .type(Notification.Type.REPLY_REPORT)
                .postId(newReplyReportEvent.getPostId())
                .replyId(newReplyReportEvent.getReplyId())
                .build();

        notificationRepository.save(newNotification);
        sendNotification(newNotification);
    }

    @Override
    public void deleteNotification(Long notificationId, Long memberId) {
        Notification findNotification = notificationRepository.findById(notificationId).
                orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_NOTIFICATION));

        if(findNotification.getMember().getMemberId() != memberId) throw new AccessDeniedException("접근 거부");

        notificationRepository.deleteById(notificationId);
    }

    @Override
    public void deleteNotifications(List<Long> notificationIds, Long memberId) {
        List<Notification> notificationList = notificationRepository.findByNotificationIdIn(notificationIds);

        Optional<Notification> wrongNotification = notificationList.stream()
                .filter(notification -> notification.getMember().getMemberId() != memberId)
                .findAny();

        if (wrongNotification.isPresent()) throw new AccessDeniedException("접근 거부");
        notificationRepository.deleteAllByIdInBatch(notificationIds);
    }

    @Override
    public Boolean findAnyNotification(Long memberId) {
        return Boolean.valueOf(notificationRepository.existsByMemberMemberId(memberId));
    }

    @Override
    public ListResponseDto<NotificationDto, Notification> findAllNotifications(Long memberId) {
        List<Notification> notificationList = notificationRepository.findAllByMemberMemberId(memberId);

        Function<Notification, NotificationDto> fn = NotificationDto::of;
        return new ListResponseDto<>(notificationList, fn);
    }
}
