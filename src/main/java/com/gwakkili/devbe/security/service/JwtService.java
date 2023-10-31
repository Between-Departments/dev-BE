package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.entity.RefreshToken;
import com.gwakkili.devbe.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {

    private final Key key;

    private final long ACCESS_TOKEN_EXPIRE_TIME;

    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.access_token_expire_time}") long accessTokenExpireTime,
                      @Value("${jwt.refresh_token_expire_time}") long refreshTokenExpireTime,
                      RefreshTokenRepository refreshTokenRepository){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime * 1000;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime * 1000;
    }

    // access token을 request에서 추출
    public String resolveAccessToken(HttpServletRequest request) {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) return null;
        return jwtHeader.replace("Bearer ", "");
    }

    public String resolveAccessToken(String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer")) return null;
        return accessToken.replace("Bearer ", "");
    }

    public String resolveAccessToken(StompHeaderAccessor headerAccessor) {
        String jwtHeader = headerAccessor.getFirstNativeHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) return null;
        return jwtHeader.replace("Bearer ", "");
    }


    public JwtTokenDto generateToken(MemberDetails memberDetails) {
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
                .setSubject(memberDetails.getMail())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // refresh 토큰 저장
        saveRefreshToken(refreshToken, memberDetails);
        return new JwtTokenDto(accessToken, refreshToken);
    }

    private void saveRefreshToken(String refreshToken, MemberDetails memberDetails) {
        RefreshToken redisRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .mail(memberDetails.getMail())
                .roles(memberDetails.getRoles())
                .expiredTime(REFRESH_TOKEN_EXPIRE_TIME / 1000)
                .build();
        refreshTokenRepository.save(redisRefreshToken);
    }

    public void deleteRefreshToken(MemberDetails memberDetails) {
        refreshTokenRepository.deleteById(memberDetails.getMail());
    }

    // // access 토큰에서 유저정보 추출
    public Authentication getAuthentication(String token) throws JwtException {
        //jwt token 복호화
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(token).getBody();
        ;
        long memberId = ((Number) claims.get("memberId")).longValue();
        String mail = claims.get("mail").toString();
        Set<Member.Role> roles = Arrays.stream(claims.get("roles").toString().split(","))
                .map(Member.Role::valueOf).collect(Collectors.toSet());
        UserDetails principal = MemberDetails.builder().memberId(memberId).mail(mail).roles(roles).build();
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    public String getMail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("mail").toString();
    }


    public RefreshToken getRefreshToken(String mail) {
        Optional<RefreshToken> result = refreshTokenRepository.findById(mail);
        if (result.isEmpty()) return null;
        return result.get();
    }

    // 토큰 유효성 검사
    public String validateToken(String token) {
        if (token == null) return "NOT_FOUND";
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return "VALID";
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return "INVALID";
    }

}
