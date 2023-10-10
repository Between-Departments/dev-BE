package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 정지 테스트")
@Transactional
public class MemberLockTest extends DevBeApplicationTests {

    String url = "/api/members/2/lock";

    @Test
    @DisplayName("회원 정지 성공")
    @WithMockMember(mail = "manager@test.ac.kr", roles = "ROLE_MANAGER", password = "a12341234!")
    public void successLockMember() throws Exception {
        mockMvc.perform(patch(url))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정지 실패: 권한없음")
    @WithMockMember(mail = "manager@test.ac.kr", roles = "ROLE_USER", password = "a12341234!")
    public void failLockMemberByForbidden() throws Exception {
        mockMvc.perform(patch(url))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }
}
