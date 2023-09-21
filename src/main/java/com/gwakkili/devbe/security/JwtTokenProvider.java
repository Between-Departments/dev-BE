package com.gwakkili.devbe.security;

import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    private final long ACCESS_TOKEN_EXPIRE_TIME;

    private final long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtTokenProvider(@Value("{jwt.secret}") String secretKey,
                            @Value("{jwt.access_token_expire_time}") long accessTokenExpireTime,
                            @Value("jwt.refresh_token_expire_time") long refreshTokenExpireTime){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
    }

    public String resolveAccessToken(HttpServletRequest request){
        String jwtHeader= request.getHeader("Authorization");
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) return null;
        return jwtHeader.replace("Bearer ", "");
    }

    public String resolveRefreshToken(HttpServletRequest request){
        String jwtHeader= request.getHeader("RefreshToken");
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) return null;
        return jwtHeader.replace("Bearer ", "");
    }

    public String generateAccessToken(MemberDto memberDto){
        return Jwts.builder()
                .setSubject(memberDto.getUsername())
                .claim("role", memberDto.getRole())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(){
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String accessToken){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        String username = claims.getSubject();
        Role role = Role.valueOf(claims.get("role").toString());
        return null;
    }

}
