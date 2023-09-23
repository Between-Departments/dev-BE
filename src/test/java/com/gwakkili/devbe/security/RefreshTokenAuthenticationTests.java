package com.gwakkili.devbe.security;

import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.entity.Role;
import com.gwakkili.devbe.repository.RefreshTokenRepository;
import com.gwakkili.devbe.security.service.JwtService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RefreshTokenAuthenticationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    JwtService jwtService;

    @Value("${jwt.secret}")
    String key;

    String uri = "/refresh";

    private void setExpireTime(long accessTokenExpireTime, long refreshExpireTime){
        this.jwtService = new JwtService(key,accessTokenExpireTime, refreshExpireTime, refreshTokenRepository);
    }

    private MemberDto getMemberDto(){
        return MemberDto.builder()
                .mail("test@awakkili.com")
                .nickname("testUser")
                .roles(Set.of(Role.ROLE_USER))
                .build();
    }

    @Test
    @DisplayName("refreshToken 인증 성공")
    public void successAuthenticationTest() throws Exception {
        //given
        setExpireTime(10, 600);
        MemberDto memberDto = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDto);
        String accessToken = jwtService.generateAccessToken(memberDto);

        //when, then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("RefreshToken", "Bearer " + refreshToken)
                 )
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().exists("RefreshToken"))
                .andDo(print());

    }

    @Test
    @DisplayName("refreshToken 인증 실패 : 유효하지 않은 토큰")
    public void failAuthenticationByInvalidToken() throws Exception {
        //given
        setExpireTime(10, 600);
        MemberDto memberDto = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDto);
        String accessToken = jwtService.generateAccessToken(memberDto);

        //when,then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken + "aa")
                .header("RefreshToken", "Bearer " + refreshToken)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("refreshToken 인증 실패 : 만료된 토큰")
    public void failAuthenticationByExpireToken() throws Exception {
        //given
        setExpireTime(1, 1);
        MemberDto memberDto = getMemberDto();
        String refreshToken = jwtService.generateRefreshToken(memberDto);
        String accessToken = jwtService.generateAccessToken(memberDto);
        Thread.sleep(1000);
        //when, then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .header("RefreshToken", "Bearer " + refreshToken)
        )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
