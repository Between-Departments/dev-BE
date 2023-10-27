package com.gwakkili.devbe.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.exception.ExceptionResponseBuilder;
import com.gwakkili.devbe.exception.dto.ExceptionDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로그인 실패시 filter 에게 호출되는 handler
 * Exception에 맞는 응답 반환
 */
@Component
@RequiredArgsConstructor
public class JwtLoginFailureHandler implements AuthenticationFailureHandler {

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        ExceptionCode exceptionCode;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            exceptionCode = ExceptionCode.BAD_CREDENTIAL;
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            exceptionCode = ExceptionCode.AUTHENTICATION_DENIED;
        } else if (exception instanceof LockedException) {
            exceptionCode = ExceptionCode.AUTHENTICATION_LOCKED;
        } else if (exception instanceof AuthenticationServiceException) {
            exceptionCode = ExceptionCode.ILLEGAL_AUTHENTICATION_FORMAT;
        } else if (exception instanceof JwtException) {
            exceptionCode = ((JwtException) exception).getExceptionCode();
        } else {
            exceptionCode = ExceptionCode.AUTHENTICATION_FAILURE;
        }
        exceptionResponseBuilder.setHttpResponse(response, exceptionCode);
    }
}
