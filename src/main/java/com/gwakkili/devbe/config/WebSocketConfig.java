package com.gwakkili.devbe.config;

import com.gwakkili.devbe.chat.handler.WebSocketErrorHandler;
import com.gwakkili.devbe.chat.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketHandler webSocketHandler;

    private final WebSocketErrorHandler webSocketErrorHandler;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //해당 파라미터의 접두사가 붙은 목적지(구독자)에 메시지를 보낼
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //엔드포인트 추가 등록
        registry//exception handler를 위한 것
                .addEndpoint("/api/endpoint")
                .setAllowedOriginPatterns("*"); //TODO: 서버 URI로 변경
        registry.setErrorHandler(webSocketErrorHandler);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketHandler);
    }

}
