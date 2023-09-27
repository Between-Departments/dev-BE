package com.gwakkili.devbe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwakkili.devbe.config.DummyDataProvider;
import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.entity.MailAuthKey;
import com.gwakkili.devbe.repository.MailAuthKeyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Import(DummyDataProvider.class)
@Transactional
public class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MailAuthKeyRepository mailAuthKeyRepository;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("회원가입 테스트")
    @Nested
    class signUpTest{
        String url = "/members";
        @Test
        @DisplayName("성공")
        public void signUpTest() throws Exception {
            //given
            MemberDto.Save saveDto = MemberDto.Save.builder()
                    .mail("test@sun.ac.kr")
                    .password("tjr1619132!")
                    .nickname("테스트멤버")
                    .school("테스트대학1")
                    .passwordConfirm("tjr1619132!")
                    .major("테스트학과1")
                    .build();
            MailAuthKey mailAuthKey = MailAuthKey.builder()
                    .mail(saveDto.getMail())
                    .Auth(true)
                    .expiredTime(60)
                    .authKey("1111")
                    .build();
            mailAuthKeyRepository.save(mailAuthKey);
            String content = objectMapper.writeValueAsString(saveDto);

            //when, then
            mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isCreated())
                    .andDo(print());
        }
        @Test
        @DisplayName("유효성 검증 실패")
        public void signUpFailTest() throws Exception {
            MemberDto.Save saveDto = MemberDto.Save.builder()
                    .mail("test123@sun.ac.kr")
                    .password("12341234")
                    .nickname("안녕하세요")
                    .school("없는대학교")
                    .passwordConfirm("12341234!")
                    .major("없는학과")
                    .build();
            String content = objectMapper.writeValueAsString(saveDto);
            //when, then
            mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }
}
