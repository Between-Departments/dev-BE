package com.gwakkili.devbe.security.handler;

import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.security.service.JwtService;
import jakarta.servlet.ServletException;
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

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MemberDto memberDto = (MemberDto) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(memberDto);
        String refreshToken = jwtService.generateRefreshToken(memberDto);
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("RefreshToken", "Bearer "+ refreshToken);
    }
}
