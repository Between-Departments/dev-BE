package com.gwakkili.devbe.chat.handler;

import com.gwakkili.devbe.exception.customExcption.IllegalJwtException;
import com.gwakkili.devbe.exception.customExcption.NotFoundJwtException;
import com.gwakkili.devbe.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
// 웹소켓은 http 요청과 다르기때문에 일반적인 security filter 로는 검증이 불가능
public class WebSocketHandler implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // stomp 메시지에서 헤더를 가져옴
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // 메시지 상태 추출
        StompCommand command = headerAccessor.getCommand();
        if (command.equals(StompCommand.CONNECT)) {
            authentication(headerAccessor);
        }
        return message;
    }

    private void authentication(StompHeaderAccessor headerAccessor) {

        // 헤더에서 access token 추출
        String authorization = headerAccessor.getFirstNativeHeader("Authorization");
        String accessToken = (authorization == null || !authorization.startsWith("Bearer")) ?
                null : authorization.replace("Bearer ", "");

        try {
            // STOMP 헤더에 회원 정보 추가
            Authentication authentication = jwtService.getAuthenticationByAccessToken(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            headerAccessor.setUser(authentication);
        } catch (SecurityException | MalformedJwtException e) {
            throw new MessageDeliveryException("INVALID_TOKEN");
        } catch (ExpiredJwtException e) {
            throw new MessageDeliveryException("EXPIRED_TOKEN");
        } catch (UnsupportedJwtException e) {
            throw new MessageDeliveryException("UNSUPPORTED_TOKEN");
        } catch (IllegalJwtException e) {
            throw new MessageDeliveryException("ILLEGAL_TOKEN");
        } catch (NotFoundJwtException e) {
            throw new MessageDeliveryException("NOT_FOUND_TOKEN");
        }
    }
}
