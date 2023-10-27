package com.gwakkili.devbe.chat.handler;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.ExceptionResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @SneakyThrows
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {

        return switch (ex.getMessage()) {
            case "INVALID" -> exceptionResponseBuilder.buildMessage(ExceptionCode.INVALID_TOKEN);
            case "EXPIRE" -> exceptionResponseBuilder.buildMessage(ExceptionCode.EXPIRED_TOKEN);
            case "NOT_FOUND_TOKEN" -> exceptionResponseBuilder.buildMessage(ExceptionCode.NOT_FOUND_TOKEN);
            case "NOT_FOUND_CHAT_ROOM" -> exceptionResponseBuilder.buildMessage(ExceptionCode.NOT_FOUND_CHAT_ROOM);
            case "ACCESS_DENIED" -> exceptionResponseBuilder.buildMessage(ExceptionCode.ACCESS_DENIED);
            case "UNAUTHORIZED" -> exceptionResponseBuilder.buildMessage(ExceptionCode.UNAUTHORIZED);
            default -> super.handleClientMessageProcessingError(clientMessage, ex);
        };
    }
}
