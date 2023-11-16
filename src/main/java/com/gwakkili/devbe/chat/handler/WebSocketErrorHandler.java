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
            case "INVALID_TOKEN" -> exceptionResponseBuilder.buildMessage(ExceptionCode.INVALID_TOKEN);
            case "EXPIRED_TOKEN" -> exceptionResponseBuilder.buildMessage(ExceptionCode.EXPIRED_TOKEN);
            case "UNSUPPORTED_TOKEN" -> exceptionResponseBuilder.buildMessage(ExceptionCode.UNSUPPORTED_TOKEN);
            case "ILLEGAL_TOKEN" -> exceptionResponseBuilder.buildMessage(ExceptionCode.ILLEGAL_TOKEN);
            case "NOT_FOUND_TOKEN" -> exceptionResponseBuilder.buildMessage(ExceptionCode.NOT_FOUND_TOKEN);
            default -> super.handleClientMessageProcessingError(clientMessage, ex);
        };
    }

}
