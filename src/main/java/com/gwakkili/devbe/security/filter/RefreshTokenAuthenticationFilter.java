package com.gwakkili.devbe.security.filter;

import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.entity.RefreshToken;
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

@Slf4j
// RefreshToken 을 이용하여 인증을 진행하는 filter
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

        // 사용자에게 받은 refreshToken에서 mail을 추출
        String mail = jwtService.getClaims(refreshToken).getSubject();

        //redis에 저장되 있는 refresh 토큰과 사용자가 보낸 refresh 토큰 비교
        RefreshToken redisRefreshToken = jwtService.getRefreshToken(mail);
        if(redisRefreshToken == null || !redisRefreshToken.getToken().equals(refreshToken))
            throw new JwtException(ExceptionCode.INVALID_TOKEN);

        //redis에 저장되어 있는 값으로 AuthenticationToken 생성
        MemberDetails memberDetails = MemberDetails.builder().mail(redisRefreshToken.getMail()).roles(redisRefreshToken.getRoles()).build();
        return new UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.getAuthorities());
    }
}
