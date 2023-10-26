package com.gwakkili.devbe.chat.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.dto.ExceptionDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        log.info(ex.getClass().getName());
        if (ex instanceof MessageDeliveryException) {
            switch (ex.getMessage()) {
                case "INVALID":
                    return createErrorMessage(ExceptionCode.INVALID_TOKEN);
                case "EXPIRE":
                    return createErrorMessage(ExceptionCode.EXPIRED_TOKEN);
                case "ACCESS_DENIED":
                    return createErrorMessage(ExceptionCode.ACCESS_DENIED);
                case "NOT_FOUND":
                    return createErrorMessage(ExceptionCode.NOT_FOUND_CHAT_ROOM);
            }
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> createErrorMessage(ExceptionCode exceptionCode) throws JsonProcessingException {

        ExceptionDto exceptionDto = new ExceptionDto(exceptionCode);
        String message = objectMapper.writeValueAsString(exceptionDto);
        log.info(message);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(message);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
