package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.config.S3Config;
import com.gwakkili.devbe.mail.entity.MailAuthKey;
import com.gwakkili.devbe.mail.repository.MailAuthKeyRepository;
import com.gwakkili.devbe.member.dto.MemberSaveDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MemberControllerTest extends DevBeApplicationTests {

    @Autowired
    MailAuthKeyRepository mailAuthKeyRepository;

    @DisplayName("회원가입 테스트")
    @Nested
    class signUpTest{
        String url = "/members";
        @Test
        @DisplayName("성공")
        public void signUpTest() throws Exception {
            //given
            MemberSaveDto saveDto = MemberSaveDto.builder()
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
            MemberSaveDto saveDto = MemberSaveDto.builder()
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

    @Test
    @DisplayName("나의 유저정보 조회")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
    public void findMyTest() throws Exception{
        //given
        String url = "/members/my";
        mockMvc.perform(get(url))
                    .andExpect(jsonPath("mail").value("test@test1.ac.kr"))
                    .andDo(print());

    }
}
