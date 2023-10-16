package com.gwakkili.devbe.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    @PostMapping("/chatRooms")
    public void saveChatRoom() {

    }

    @GetMapping("/chatRooms")
    public void getChatRoomList() {

    }

    @DeleteMapping("/chatRooms/{chatRoomId}")
    public void deleteChatRoom(@PathVariable long chatRoomId) {

    }

    @GetMapping("/chatRooms/{chatRoomId}/messages")
    public void getChatMessageList(@PathVariable long chatRoomId) {

    }

    @MessageMapping("/chatRooms/{chatRoomId}/message")
    @SendTo("/chatRoom/{roomId}")
    public void sendChatMessage() {

    }
}
