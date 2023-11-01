package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.exception.customExcption.IllegalJwtException;
import com.gwakkili.devbe.exception.customExcption.NotFoundJwtException;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.entity.RefreshToken;
import com.gwakkili.devbe.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final Key key;

    private final long ACCESS_TOKEN_EXPIRE_TIME;

    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtServiceImpl(@Value("${jwt.secret}") String secretKey,
                          @Value("${jwt.access_token_expire_time}") long accessTokenExpireTime,
                          @Value("${jwt.refresh_token_expire_time}") long refreshTokenExpireTime,
                          RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime * 1000;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime * 1000;
    }


    public JwtTokenDto generateToken(Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String roles = memberDetails.getRoles().stream()
                .map(Member.Role::name)
                .collect(Collectors.joining(","));
        // accessToken 생성
        String accessToken = Jwts.builder()
                .claim("memberId", memberDetails.getMemberId())
                .claim("mail", memberDetails.getMail())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refresh 토큰 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .claim("memberId", memberDetails.getMemberId())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // refresh 토큰 저장
        return new JwtTokenDto(accessToken, refreshToken);
    }

    public void saveRefreshToken(String refreshToken, Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        RefreshToken redisRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .mail(memberDetails.getMail())
                .memberId(memberDetails.getMemberId())
                .roles(memberDetails.getRoles())
                .expiredTime(REFRESH_TOKEN_EXPIRE_TIME / 1000)
                .build();
        refreshTokenRepository.save(redisRefreshToken);
    }


    public void deleteRefreshToken(long memberId) {
        refreshTokenRepository.deleteById(memberId);
    }

    // // access 토큰에서 유저정보 추출
    public Authentication getAuthenticationByAccessToken(String accessToken) {
        //jwt token 복호화
        Claims claims = getClaims(accessToken);
        long memberId = ((Number) claims.get("memberId")).longValue();
        String mail = claims.get("mail").toString();
        Set<Member.Role> roles = Arrays.stream(claims.get("roles").toString().split(","))
                .map(Member.Role::valueOf).collect(Collectors.toSet());
        UserDetails principal = MemberDetails.builder().memberId(memberId).mail(mail).roles(roles).build();
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) throw new NotFoundJwtException("트콘을 찾을 수 없습니다.");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (IllegalArgumentException e) {
            throw new IllegalJwtException("잘못된 토큰 입니다.");
        }
    }

    public Authentication getAuthenticationByRefreshToken(String refreshToken) {
        Claims claims = getClaims(refreshToken);
        long memberId = (long) claims.get("memberId");
        RefreshToken redisRefreshToken = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundJwtException("토큰을 찾을 수 없습니다"));
        if (!redisRefreshToken.getToken().equals(refreshToken)) throw new SecurityException("유효하지 않은 토큰 입니다.");
        UserDetails principal = MemberDetails.builder()
                .memberId(redisRefreshToken.getMemberId())
                .mail(redisRefreshToken.getMail())
                .roles(redisRefreshToken.getRoles())
                .build();
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

}
