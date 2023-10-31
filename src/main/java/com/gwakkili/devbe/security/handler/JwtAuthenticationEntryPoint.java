package com.gwakkili.devbe.security.handler;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.ExceptionResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ExceptionResponseBuilder exceptionResponseBuilder;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException{

        exceptionResponseBuilder.setHttpResponse(response, ExceptionCode.UNAUTHORIZED);
    }
}
