package com.gwakkili.devbe.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.dto.ExceptionDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException{

        if(exception instanceof JwtException e){
            setResponse(response, e.getExceptionCode());
        }else {
            setResponse(response, ExceptionCode.UNAUTHORIZED);
        }
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
