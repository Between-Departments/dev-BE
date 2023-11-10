package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.member.dto.request.PasswordDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원탈퇴 테스트")
@Transactional
public class deleteMemberTests extends DevBeApplicationTests {

    @Nested
    @DisplayName("회원 탈퇴(일반 회원)")
    class deleteMember {

        String url = "/api/members/my";

        @DisplayName("성공")
        @WithMockMember
        @Test
        public void success() throws Exception {

            //given
            PasswordDto passwordDto = new PasswordDto("a12341234!");
            String content = objectMapper.writeValueAsString(passwordDto);

            //when, then
            mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }


        @DisplayName("실패: 비밀번호 유효성 검증 실패")
        @WithMockMember
        @Test
        public void failByInvalidPassword() throws Exception {

            //given
            PasswordDto passwordDto = new PasswordDto("12342!!");
            String content = objectMapper.writeValueAsString(passwordDto);

            String url = "/api/members/my";
            mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("code").value(ExceptionCode.INVALID_INPUT_VALUE.getCode()))
                    .andExpect(jsonPath("fieldErrors.password").exists())
                    .andDo(print());
        }

        @DisplayName("실패: 비밀번호 불일치")
        @WithMockMember
        @Test
        public void failByNotMatchPassword() throws Exception {

            //given
            PasswordDto passwordDto = new PasswordDto("b123412554!");
            String content = objectMapper.writeValueAsString(passwordDto);

            String url = "/api/members/my";
            mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("code").value(ExceptionCode.INVALID_PASSWORD.getCode()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("회원 탈퇴: 매니저")
    class deleteMemberByManager {
        String url = "/api/members/{memberId}";

        @DisplayName("성공")
        @WithMockMember(roles = "ROLE_MANAGER")
        @Test
        public void success() throws Exception {

            //given
            long memberId = 5;

            //when, then
            mockMvc.perform(delete(url, memberId))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }

        @DisplayName("실패: 회원을 찾을 수 없음")
        @WithMockMember(roles = "ROLE_MANAGER")
        @Test
        public void failByNotFound() throws Exception {

            //given
            long memberId = 500;

            //when, then
            mockMvc.perform(delete(url, memberId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_MEMBER.getCode()))
                    .andDo(print());

        }

        @DisplayName("실패: 권한 없음")
        @WithMockMember(roles = "ROLE_USER")
        @Test
        public void failByAccessDined() throws Exception {

            //given
            long memberId = 5;

            //when, then
            mockMvc.perform(delete(url, memberId))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                    .andDo(print());

        }

    }


}
