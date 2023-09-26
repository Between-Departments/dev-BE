package com.gwakkili.devbe.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.security.dto.MemberDetails;
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
            MemberDetails memberDetails = objectMapper.readValue(request.getInputStream(), MemberDetails.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDetails.getUsername(), memberDetails.getPassword());
            return super.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("잘못된 로그인 요청 형식입니다.");
        }
    }
}
