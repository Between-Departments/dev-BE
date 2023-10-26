package com.gwakkili.devbe.notification.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StompNotificationController {

    private final SimpMessageSendingOperations messagingTemplate;


    @MessageMapping("/notificaitons/{userId}")
    public void message(@DestinationVariable("userId") Long userId){
        messagingTemplate.convertAndSend("/sub/notifications/"+ userId,"alarm socket connection completed.");
    }


}
