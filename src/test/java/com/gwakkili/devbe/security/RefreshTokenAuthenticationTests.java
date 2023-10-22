package com.gwakkili.devbe.security;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.security.repository.RefreshTokenRepository;
import com.gwakkili.devbe.security.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("refresh token 인증 테스트")
@Transactional
public class RefreshTokenAuthenticationTests extends DevBeApplicationTests {


    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    JwtService jwtService;

    @Value("${jwt.secret}")
    String key;

    String uri = "/api/refresh";

    private void setExpireTime(long accessTokenExpireTime, long refreshExpireTime){
        this.jwtService = new JwtService(key,accessTokenExpireTime, refreshExpireTime, refreshTokenRepository);
    }

    private MemberDetails getMemberDto(){
        return MemberDetails.builder()
                .mail("test@awakkili.com")
                .roles(Set.of(Member.Role.ROLE_USER))
                .build();
    }

    @Test
    @DisplayName("refreshToken 인증 성공")
    public void successAuthenticationTest() throws Exception {
        //given
        setExpireTime(10, 600);
        MemberDetails memberDetails = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDetails);
        String accessToken = jwtService.generateAccessToken(memberDetails);
        Cookie cookie = new Cookie("RefreshToken", refreshToken);
        cookie.setPath("/api/refresh");
        cookie.setMaxAge(600);
        cookie.setHttpOnly(true);

        //when, then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .cookie(cookie)
        )
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(cookie().exists("RefreshToken"))
                .andDo(print());

    }

    @Test
    @DisplayName("refreshToken 인증 실패 : 유효하지 않은 토큰")
    public void failAuthenticationByInvalidToken() throws Exception {
        //given
        setExpireTime(10, 600);
        MemberDetails memberDetails = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDetails);
        String accessToken = jwtService.generateAccessToken(memberDetails);

        Cookie cookie = new Cookie("RefreshToken", refreshToken);
        cookie.setPath("/api/refresh");
        cookie.setMaxAge(600);
        cookie.setHttpOnly(true);

        //when,then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken + "aa")
                .cookie(cookie)
        )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("refreshToken 인증 실패 : 만료된 토큰")
    public void failAuthenticationByExpireToken() throws Exception {
        //given
        setExpireTime(1, 1);
        MemberDetails memberDetails = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDetails);
        String accessToken = jwtService.generateAccessToken(memberDetails);

        Cookie cookie = new Cookie("RefreshToken", refreshToken);
        cookie.setPath("/api/refresh");
        cookie.setMaxAge(600);
        cookie.setHttpOnly(true);

        Thread.sleep(1000);
        //when, then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .cookie(cookie)
        )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("refreshToken 인증 실패 : 토큰을 찾을 수 없음")
    public void failAuthenticationByNotFoundToken() throws Exception {
        //when, then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
