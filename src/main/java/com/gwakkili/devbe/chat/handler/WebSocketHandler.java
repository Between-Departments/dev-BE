package com.gwakkili.devbe.chat.handler;

import com.gwakkili.devbe.chat.service.ChatService;
import com.gwakkili.devbe.exception.customExcption.AccessDeniedException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.core.DestinationResolutionException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@RequiredArgsConstructor
@Component
@Slf4j
// 웹소켓은 http 요청과 다르기때문에 일반적인 security filter 로는 검증이 불가능
public class WebSocketHandler implements ChannelInterceptor {

    private final JwtService jwtService;

    private final ChatService chatService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // stomp 메시지에서 헤더를 가져옴
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // 메시지 상태 추출
        StompCommand command = headerAccessor.getCommand();
        if (command.equals(StompCommand.CONNECT)) {
            authentication(headerAccessor);
        } else if (command.equals(StompCommand.SUBSCRIBE)) {
            subscribeChatRoom(headerAccessor);
        }
        return message;
    }

    private void authentication(StompHeaderAccessor headerAccessor) {

        // 헤더에서 access token 추출
        String accessToken = jwtService.resolveAccessToken(headerAccessor);

        // 토큰 유효성 검사
        String validateAccessToken = jwtService.validateToken(accessToken);
        if (validateAccessToken.equals("INVALID")) {
            throw new MessageDeliveryException("INVALID");
        } else if (validateAccessToken.equals("EXPIRE")) {
            throw new MessageDeliveryException("EXPIRE");
        }
        // STOMP 헤더에 유정정보 추가
        Authentication authentication = jwtService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        headerAccessor.setUser(authentication);
    }

    private void subscribeChatRoom(StompHeaderAccessor headerAccessor) {

        // 채팅방 구독 요청 인지 검사
        String url = "/api/sub/chat/rooms/{roomId}";
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String destination = headerAccessor.getDestination();
        if (destination == null) throw new DestinationResolutionException("");
        if (!antPathMatcher.match(url, destination)) return;

        // 채팅방 번호 추출
        long roomId = Long.parseLong(antPathMatcher.extractUriTemplateVariables(url, destination).get("roomId"));
        Authentication authentication = (Authentication) headerAccessor.getUser();

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        //채팅방 입장 및 예외 처리
        try {
            chatService.updateChatMessageIsRead(roomId, memberDetails.getMemberId());
        } catch (AccessDeniedException e) {
            throw new MessageDeliveryException("ACCESS_DENIED");
        } catch (NotFoundException e) {
            throw new MessageDeliveryException("NOT_FOUND");
        }
    }
}
