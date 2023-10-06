package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.security.entity.RefreshToken;
import com.gwakkili.devbe.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    public String resolveAccessToken(HttpServletRequest request){
        String jwtHeader= request.getHeader("Authorization");
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) return null;
        return jwtHeader.replace("Bearer ", "");
    }

    // refresh token을 request에서 추출
    public String resolveRefreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RefreshToken"))
                return cookie.getValue();
        }
        return null;
    }

    // access token 생성
    public String generateAccessToken(MemberDetails memberDetails){
        String roles = memberDetails.getRoles().stream()
                .map(Member.Role::name)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(memberDetails.getUsername())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // refresh token 생성
    public String generateRefreshToken(MemberDetails memberDetails){

        log.info("rt 만료시간: {}",REFRESH_TOKEN_EXPIRE_TIME);
        // refresh 토큰 생성
        String token = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .setSubject(memberDetails.getUsername())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // refresh 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .mail(memberDetails.getMail())
                .roles(memberDetails.getRoles())
                .expiredTime(REFRESH_TOKEN_EXPIRE_TIME/1000)
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    // // access 토큰에서 유저정보 추출
    public Authentication getAuthentication(String token) throws JwtException{
        //jwt token 복호화
        Claims claims = getClaims(token);
        String mail = claims.getSubject();
        Set<Member.Role> roles = Arrays.stream(claims.get("roles").toString().split(","))
                .map(Member.Role::valueOf).collect(Collectors.toSet());
        UserDetails principal = MemberDetails.builder().mail(mail).roles(roles).build();
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public RefreshToken getRefreshToken(String mail){
        Optional<RefreshToken> result = refreshTokenRepository.findById(mail);
        if(result.isEmpty()) return null;
        return result.get();
    }

    // 토큰 유효성 검사
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return "VALID";
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return "EXPIRE";
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return "INVALID";
    }

    public long getRefreshTokenExpireTime() {
        return REFRESH_TOKEN_EXPIRE_TIME;
    }
}
