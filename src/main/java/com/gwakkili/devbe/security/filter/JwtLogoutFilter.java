package com.gwakkili.devbe.security.filter;

import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtLogoutFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/logout", "POST");

    public JwtLogoutFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!DEFAULT_ANT_PATH_REQUEST_MATCHER.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.resolveAccessToken(request);
        if (!jwtService.validateToken(accessToken).equals("VALID")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = jwtService.getAuthentication(accessToken);
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        jwtService.deleteRefreshToken(memberDetails);
        Cookie cookie = new Cookie("RefreshToken", null);
        cookie.setMaxAge(1);

        response.addCookie(cookie);
    }
}
