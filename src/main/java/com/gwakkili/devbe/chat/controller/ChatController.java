package com.gwakkili.devbe.chat.controller;

import com.gwakkili.devbe.chat.dto.Request.SaveChatMessageDto;
import com.gwakkili.devbe.chat.dto.Request.SaveChatRoomDto;
import com.gwakkili.devbe.chat.dto.Response.ChatMessageDto;
import com.gwakkili.devbe.chat.dto.Response.ChatRoomDto;
import com.gwakkili.devbe.chat.entity.ChatMessage;
import com.gwakkili.devbe.chat.service.ChatService;
import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "채팅 api")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    private final SimpUserRegistry userRegistry;

    @PostMapping("/rooms")
    @Operation(summary = "채팅방 생성")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomDto saveChatRoom(@AuthenticationPrincipal MemberDetails memberDetails,
                                    @RequestBody SaveChatRoomDto saveChatRoomDto) {
        saveChatRoomDto.setMasterId(memberDetails.getMemberId());
        return chatService.saveChatRoom(saveChatRoomDto);
    }

    @GetMapping("/rooms")
    @Operation(summary = "채팅방 목록 조회")
    @PreAuthorize("isAuthenticated()")
    public SliceResponseDto<ChatRoomDto, Object[]> getChatRoomList(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                   @ParameterObject SliceRequestDto sliceRequestDto) {
        return chatService.getChatRoomList(memberDetails.getMemberId(), sliceRequestDto);
    }

    @DeleteMapping("/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "채팅방 삭제")
    @PreAuthorize("isAuthenticated()")
    public void deleteChatRoom(@AuthenticationPrincipal MemberDetails memberDetails,
                               @Parameter(description = "채팅방 번호", in = ParameterIn.PATH) @PathVariable long roomId) {
        chatService.deleteChatRoom(roomId, memberDetails.getMemberId());
    }

    @GetMapping("/rooms/{roomId}/messages")
    @Operation(summary = "채팅 메시지 목록 조회")
    @PreAuthorize("isAuthenticated()")
    public SliceResponseDto<ChatMessageDto, ChatMessage> getChatMessageList(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                            @Parameter(description = "채팅방 번호", in = ParameterIn.PATH) @PathVariable long roomId,
                                                                            @ParameterObject SliceRequestDto sliceRequestDto) {
        return chatService.getChatMessageList(roomId, memberDetails.getMemberId(), sliceRequestDto);
    }


    @MessageMapping("/chat/rooms/{roomId}/message")
    @SendTo("/api/sub/chat/rooms/{roomId}")
    public ChatMessageDto sendChatMessage(@DestinationVariable long roomId, Authentication authentication, SaveChatMessageDto saveChatMessageDto) {
        log.info("{}번 채팅방에 메시지 전송", roomId);
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        int memberNumInRoom = userRegistry.findSubscriptions(s -> s.getDestination().matches("/api/sub/chat/rooms/" + roomId)).size();
        saveChatMessageDto.setChatRoomId(roomId);
        saveChatMessageDto.setSenderId(memberDetails.getMemberId());
        return chatService.saveChatMessage(saveChatMessageDto, memberNumInRoom);
    }
}
