package com.gwakkili.devbe.security.filter;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.JwtException;
import com.gwakkili.devbe.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
// access token을 이용하여
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtService jwtService,
                                  AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String accessToken = jwtService.resolveAccessToken(request);

        //access token 유효성 검증
        String validateAccessToken = jwtService.validateToken(accessToken);
        AuthenticationEntryPoint authenticationEntryPoint = getAuthenticationEntryPoint();
        // access token 이 유효하지 않으면 예외 발생
        if(validateAccessToken.equals("INVALID")) {
            chain.doFilter(request, response);
            return;
            // access token 이 만료되었다면 예외 발생
        }else if(validateAccessToken.equals("EXPIRE")){
            authenticationEntryPoint.commence(request, response, new JwtException(ExceptionCode.EXPIRED_TOKEN));
        }
        // 유효한 사용자라면 authentication을 만들고 SecurityContext 등록
        Authentication authentication = jwtService.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
