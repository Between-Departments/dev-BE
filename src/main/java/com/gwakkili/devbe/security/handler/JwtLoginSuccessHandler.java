package com.gwakkili.devbe.security.handler;

import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
/**
 * 로그인 성공시 filter 에게 호출되는 handler
 * 응답 header에 access token, refresh token을 추가한 후 반환
 */
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(memberDetails);
        String refreshToken = jwtService.generateRefreshToken(memberDetails);

        Cookie cookie = new Cookie("RefreshToken", "Bearer " + refreshToken);
        cookie.setPath("/api/refresh");
        cookie.setMaxAge((int) jwtService.getRefreshTokenExpireTime() / 1000);
        cookie.setHttpOnly(true);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(cookie);
    }
}
