package com.gwakkili.devbe.security;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.repository.RefreshTokenRepository;
import com.gwakkili.devbe.security.service.JwtProperties;
import com.gwakkili.devbe.security.service.JwtServiceImpl;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("토큰 재발급 테스트")
public class refreshTokenTests extends DevBeApplicationTests {

    String url = "/api/refresh";

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private JwtServiceImpl jwtService;

    private final String SECRET_KEY = "testsecrettestsecrettestsecrettestsecrettestsecret";


    private JwtTokenDto getJwtToken() {
        MemberDetails principal = MemberDetails.builder()
                .memberId(1)
                .mail("manager@test1.ac.kr")
                .nickname("테스트멤버")
                .roles(Set.of(Member.Role.ROLE_MANAGER))
                .build();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        JwtTokenDto jwtTokenDto = jwtService.generateToken(authenticationToken);
        jwtService.saveRefreshToken(jwtTokenDto.getRefreshToken(), authenticationToken);
        return jwtTokenDto;
    }

    @Test
    @DisplayName("성공")
    public void success() throws Exception {

        //given
        JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, 10, 10);
        this.jwtService = new JwtServiceImpl(jwtProperties, refreshTokenRepository);
        JwtTokenDto jwtToken = getJwtToken();

        //when, then
        mockMvc.perform(post(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken.getAccessToken())
                .cookie(new Cookie("RefreshToken", jwtToken.getRefreshToken())))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(cookie().exists("RefreshToken"))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 유효하지 않은 토큰")
    public void failByInvalidToken() throws Exception {

        //given
        JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, 10, 10);
        this.jwtService = new JwtServiceImpl(jwtProperties, refreshTokenRepository);
        JwtTokenDto jwtToken = getJwtToken();

        //when, then
        mockMvc.perform(post(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken.getAccessToken() + "123")
                .cookie(new Cookie("RefreshToken", jwtToken.getRefreshToken() + "123")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value(ExceptionCode.INVALID_TOKEN.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 만료된 토큰")
    public void failByExpireToken() throws Exception {
        //given
        JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, 1, 1);
        this.jwtService = new JwtServiceImpl(jwtProperties, refreshTokenRepository);
        JwtTokenDto jwtToken = getJwtToken();

        //when, then
        Thread.sleep(1000);
        mockMvc.perform(post(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken.getAccessToken())
                .cookie(new Cookie("RefreshToken", jwtToken.getRefreshToken())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value(ExceptionCode.EXPIRED_TOKEN.getCode()))
                .andDo(print());
    }

}
