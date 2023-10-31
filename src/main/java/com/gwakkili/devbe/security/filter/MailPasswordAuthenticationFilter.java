package com.gwakkili.devbe.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.security.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
// 메일과 비밀번호를 이용하여 인증을 진행하는 filter
public class MailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public MailPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                            AuthenticationSuccessHandler successHandler,
                                            AuthenticationFailureHandler failureHandler) {

        super(new AntPathRequestMatcher("/api/login", "POST"), authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //로그인 요청 Method가 Post가 아니면
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getMail(), loginDto.getPassword());
            return super.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("잘못된 로그인 요청 형식입니다.");
        }
    }
}
