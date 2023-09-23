package com.gwakkili.devbe.security.filter;

import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.entity.RefreshToken;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.JwtException;
import com.gwakkili.devbe.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Slf4j
public class RefreshTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtService jwtService;

    public RefreshTokenAuthenticationFilter(JwtService jwtService,
                                            AuthenticationSuccessHandler successHandler,
                                            AuthenticationFailureHandler failureHandler) {
        super(new AntPathRequestMatcher("/refresh", "POST"));
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
        this.jwtService = jwtService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // access token 과 refresh token 을 헤더에서 추출
        String accessToken = jwtService.resolveAccessToken(request);
        String refreshToken = jwtService.resolveRefreshToken(request);

        // access token 과 refresh token 유효성 검사
        if(refreshToken == null || accessToken == null){
            throw new JwtException(ExceptionCode.NOT_FOUND_TOKEN);
        }else if(jwtService.validateToken(refreshToken).equals("INVALID") || jwtService.validateToken(accessToken).equals("INVALID")){
            throw new JwtException(ExceptionCode.INVALID_TOKEN);
        }else if(jwtService.validateToken(refreshToken).equals("EXPIRE")){
            throw new JwtException(ExceptionCode.EXPIRED_TOKEN);
        }

        String mail = jwtService.getClaims(refreshToken).getSubject();
        RefreshToken redisRefreshToken = jwtService.getRefreshToken(mail);
        //redis에 저장되 있는 refresh 토큰과 사용자가 보낸 refresh 토큰 비교
        if(redisRefreshToken == null || !redisRefreshToken.getToken().equals(refreshToken))
            throw new JwtException(ExceptionCode.INVALID_TOKEN);
        MemberDto memberDto = MemberDto.builder().mail(redisRefreshToken.getMail()).roles(redisRefreshToken.getRoles()).build();
        return new UsernamePasswordAuthenticationToken(memberDto, "", memberDto.getAuthorities());
    }
}
