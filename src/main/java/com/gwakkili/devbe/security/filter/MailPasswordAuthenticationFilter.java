package com.gwakkili.devbe.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.IllegalAuthenticationFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
public class MailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public MailPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                            AuthenticationSuccessHandler successHandler,
                                            AuthenticationFailureHandler failureHandler) {
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //로그인 요청 Method가 Post가 아니면
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MemberDto memberDto = objectMapper.readValue(request.getInputStream(), MemberDto.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDto.getUsername(), memberDto.getPassword());
            return super.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("잘못된 로그인 요청 형식입니다.");
        }
    }
}
