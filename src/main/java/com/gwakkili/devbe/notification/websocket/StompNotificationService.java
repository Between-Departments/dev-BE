package com.gwakkili.devbe.notification.websocket;

import com.gwakkili.devbe.notification.event.NewReplyEvent;
import com.gwakkili.devbe.reply.dto.response.ReplyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StompNotificationService {

    private final SimpMessageSendingOperations messagingTemplate;

    public void notify(ReplyDto replyDto){
        messagingTemplate.convertAndSend("/sub/notifications" + replyDto.getPostWriterId(), new NewReplyEvent(replyDto.getContent()));
    }
}