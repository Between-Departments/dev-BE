package com.gwakkili.devbe.security;

import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.entity.RefreshToken;
import com.gwakkili.devbe.entity.Member.Role;
import com.gwakkili.devbe.repository.RefreshTokenRepository;
import com.gwakkili.devbe.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Set;

@SpringBootTest
public class JwtServiceTests {

    private JwtService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private Key key;


    @BeforeEach
    void setUp() {
        long accessTokenExpireTime = 1000*60;
        long refreshTokenExpireTime = 1000*60;
        String key = "sdaddddddddddddsaaaaaaaaaaasddddddwwwwwwwwwadds";
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtService = new JwtService(key, accessTokenExpireTime, refreshTokenExpireTime, refreshTokenRepository);
    }

    private MemberDto getMemberDto(){
        return MemberDto.builder()
                .mail("test@awakkili.com")
                .roles(Set.of(Role.ROLE_USER))
                .build();
    }

    @Test
    @DisplayName("access token 생성")
    void generateAccessTokenTest(){
        //given
        MemberDto memberDto = getMemberDto();
        //when
        String accessToken = jwtService.generateAccessToken(memberDto);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(accessToken).getBody();

        //then
        Assertions.assertThat(claims.getSubject()).isEqualTo(memberDto.getUsername());
    }

    @Test
    @DisplayName("refreshToken 생성")
    void generateRefreshTokenTest(){
        //given
        MemberDto memberDto = getMemberDto();

        String accessToken = jwtService.generateAccessToken(memberDto);
        //when
        String refreshToken = jwtService.generateRefreshToken(memberDto);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(refreshToken).getBody();
        //then
        Assertions.assertThat(claims).isNotEmpty();
    }

    @Test
    @DisplayName("authentication 생성")
    void getAuthenticationTest(){

        //given
        MemberDto memberDto = getMemberDto();

        String accessToken = jwtService.generateAccessToken(memberDto);

        //when
        Authentication authentication = jwtService.getAuthentication(accessToken);
        MemberDto expectedMemberDto = (MemberDto)authentication.getPrincipal();

        //then
        Assertions.assertThat(memberDto).usingRecursiveComparison().isEqualTo(expectedMemberDto);
    }

    @Test
    @DisplayName("refreshToken 조회")
    void getRefreshToken(){

        //given
        MemberDto memberDto = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDto);

        //when
        RefreshToken findRefreshToken = jwtService.getRefreshToken(memberDto.getMail());

        //then
        Assertions.assertThat(findRefreshToken.getToken()).isEqualTo(refreshToken);
        Assertions.assertThat(findRefreshToken.getMail()).isEqualTo(memberDto.getMail());

    }


}
