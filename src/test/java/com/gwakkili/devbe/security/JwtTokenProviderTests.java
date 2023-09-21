package com.gwakkili.devbe.security;

import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Set;

public class JwtTokenProviderTests {

    private static JwtTokenProvider jwtTokenProvider;
    private static Key key;

    @BeforeEach
    void setUp() {
        long accessTokenExpireTime = 1000*60;
        long refreshTokenExpireTime = 1000*60;
        String key = "sdaddddddddddddsaaaaaaaaaaasddddddwwwwwwwwwadds";
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        jwtTokenProvider = new JwtTokenProvider(key, accessTokenExpireTime, refreshTokenExpireTime);
    }

    @Test
    @DisplayName("access token 생성")
    void generateAccessTokenTest(){
        //given
        MemberDto memberDto = MemberDto.builder()
                .username("testMember")
                .roleSet(Set.of(Role.ROLE_USER))
                .build();
        //when
        String accessToken = jwtTokenProvider.generateAccessToken(memberDto);

        //then
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        Assertions.assertThat(claims.getSubject()).isEqualTo(memberDto.getUsername());
        //Assertions.assertThat(claims.get("role")).isEqualTo(memberDto.getRole().name());
    }

    @Test
    @DisplayName("refreshToken 생성")
    void generateRefreshToken(){

        //when
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        //then
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(refreshToken).getBody();
        Assertions.assertThat(claims).isNotEmpty();
    }


}
