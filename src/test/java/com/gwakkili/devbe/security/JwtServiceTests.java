package com.gwakkili.devbe.security;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.config.DummyDataProvider;
import com.gwakkili.devbe.config.S3MockConfig;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.security.entity.RefreshToken;
import com.gwakkili.devbe.security.repository.RefreshTokenRepository;
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
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Set;

@SpringBootTest
@DisplayName("JWT 서비스 테스트")
@Transactional
public class JwtServiceTests extends DevBeApplicationTests {

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

    private MemberDetails getMemberDto(){
        return MemberDetails.builder()
                .mail("test@awakkili.com")
                .roles(Set.of(Member.Role.ROLE_USER))
                .build();
    }

    @Test
    @DisplayName("access token 생성")
    void generateAccessTokenTest(){
        //given
        MemberDetails memberDetails = getMemberDto();
        //when
        String accessToken = jwtService.generateAccessToken(memberDetails);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(accessToken).getBody();

        //then
        Assertions.assertThat(claims.getSubject()).isEqualTo(memberDetails.getUsername());
    }

    @Test
    @DisplayName("refreshToken 생성")
    void generateRefreshTokenTest(){
        //given
        MemberDetails memberDetails = getMemberDto();

        String accessToken = jwtService.generateAccessToken(memberDetails);
        //when
        String refreshToken = jwtService.generateRefreshToken(memberDetails);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(refreshToken).getBody();
        //then
        Assertions.assertThat(claims).isNotEmpty();
    }

    @Test
    @DisplayName("authentication 생성")
    void getAuthenticationTest(){

        //given
        MemberDetails memberDetails = getMemberDto();

        String accessToken = jwtService.generateAccessToken(memberDetails);

        //when
        Authentication authentication = jwtService.getAuthentication(accessToken);
        MemberDetails expectedMemberDetails = (MemberDetails)authentication.getPrincipal();

        //then
        Assertions.assertThat(memberDetails).usingRecursiveComparison().isEqualTo(expectedMemberDetails);
    }

    @Test
    @DisplayName("refreshToken 조회")
    void getRefreshToken(){

        //given
        MemberDetails memberDetails = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDetails);

        //when
        RefreshToken findRefreshToken = jwtService.getRefreshToken(memberDetails.getMail());

        //then
        Assertions.assertThat(findRefreshToken.getToken()).isEqualTo(refreshToken);
        Assertions.assertThat(findRefreshToken.getMail()).isEqualTo(memberDetails.getMail());

    }


}
