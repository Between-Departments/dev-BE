package com.gwakkili.devbe.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.entity.Member;
import com.gwakkili.devbe.entity.Role;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MailPasswordAuthenticationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("로그인 테스트")
    class loginTest{

        private String uri ="/login";

        private Member makeMember(boolean locked){
            return   Member.builder()
                    .mail("member@test.com")
                    .nickname("member")
                    .roles(Set.of(Role.ROLE_USER))
                    .password(passwordEncoder.encode("1111"))
                    .locked(locked)
                    .build();

        }

        @Test
        @DisplayName("로그인 성공")
        public void loginSuccessTest() throws Exception {
            //given
            Member member = makeMember(false);

            memberRepository.save(member);

            Map<String, String> map = Map.of("mail", member.getMail(), "password", "1111");
            String content = objectMapper.writeValueAsString(map);

            //when, then
            mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Authorization"))
                    .andExpect(header().exists("RefreshToken"))
                    .andDo(print());
        }

        @Test
        @DisplayName("로그인 실패 : 메일 및 비밀번호 불일치")
        public void loginFailTest() throws Exception {
            //given
            Member member =  makeMember(false);

            memberRepository.save(member);
            Map<String, String> map = Map.of("mail", member.getMail(), "password", "1234");
            String content = objectMapper.writeValueAsString(map);

            //when, then
            mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("code").value(ExceptionCode.BAD_CREDENTIAL.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.BAD_CREDENTIAL.getMessage()))
                    .andDo(print());

        }

        @Test
        @DisplayName("로그인 실패 : 정지된 계정")
        public void loginFailTest2() throws Exception {
            //given
            Member member =  makeMember(true);

            memberRepository.save(member);
            Map<String, String> map = Map.of("mail", member.getMail(), "password", "1111");
            String content = objectMapper.writeValueAsString(map);

            //when, then
            mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("code").value(ExceptionCode.AUTHENTICATION_LOCKED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.AUTHENTICATION_LOCKED.getMessage()))
                    .andDo(print());

        }
    }
}
