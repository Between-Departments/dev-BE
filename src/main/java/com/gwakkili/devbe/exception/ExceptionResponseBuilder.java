package com.gwakkili.devbe.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.exception.dto.ExceptionDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ExceptionResponseBuilder {

    private final ObjectMapper objectMapper;

    public void setHttpResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {

        ExceptionDto ExceptionDto = new ExceptionDto(exceptionCode);
        String responseBody = objectMapper.writeValueAsString(ExceptionDto);

        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }

    public Message<byte[]> buildMessage(ExceptionCode exceptionCode) throws JsonProcessingException {

        ExceptionDto exceptionDto = new ExceptionDto(exceptionCode);
        String message = objectMapper.writeValueAsString(exceptionDto);
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(message);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
