package com.gwakkili.devbe.security;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.security.dto.LoginDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("로그인 테스트")
public class loginTests extends DevBeApplicationTests {

    String url = "/api/login";

    @Test
    @DisplayName("성공")
    public void success() throws Exception {
        //given
        LoginDto loginDto = new LoginDto("test@test1.ac.kr", "a12341234!");
        String content = objectMapper.writeValueAsString(loginDto);
        //when, then
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(cookie().exists("RefreshToken"))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 아이디 및 비밀번호 불일치")
    public void failByBadCredential() throws Exception {
        //given
        LoginDto loginDto = new LoginDto("test@test1.ac.krr", "a12341234!!");
        String content = objectMapper.writeValueAsString(loginDto);

        //when, then
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value(ExceptionCode.BAD_CREDENTIAL.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.BAD_CREDENTIAL.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 계정정지")
    public void failByLock() throws Exception {
        //given
        LoginDto loginDto = new LoginDto("test@test1.ac.kr", "a12341234!");
        String content = objectMapper.writeValueAsString(loginDto);

        //when, then
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value(ExceptionCode.AUTHENTICATION_LOCKED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.AUTHENTICATION_LOCKED.getMessage()))
                .andDo(print());
    }

}
