package com.gwakkili.devbe.security.handler;

import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
/**
 * 로그인 성공시 filter 에게 호출되는 handler
 * 응답 header에 access token, refresh token을 추가한 후 반환
 */
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(memberDetails);
        String refreshToken = jwtService.generateRefreshToken(memberDetails);

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .domain(".dev-fe-between.vercel.app")
                .path("/api/refresh")
                .maxAge((int) jwtService.getRefreshTokenExpireTime() / 1000)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Set-Cookie", responseCookie.toString());
    }
}
