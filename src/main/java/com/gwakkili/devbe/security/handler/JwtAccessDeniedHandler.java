package com.gwakkili.devbe.security.handler;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.ExceptionResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {
        exceptionResponseBuilder.setHttpResponse(response, ExceptionCode.ACCESS_DENIED);
    }

}
