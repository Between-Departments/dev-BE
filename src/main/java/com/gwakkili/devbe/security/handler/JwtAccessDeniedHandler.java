package com.gwakkili.devbe.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.exception.dto.ExceptionDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        setResponse(response, ExceptionCode.ACCESS_DENIED);
    }

    private void setResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionDto ExceptionDto = new ExceptionDto(exceptionCode);
        String responseBody = objectMapper.writeValueAsString(ExceptionDto);

        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
