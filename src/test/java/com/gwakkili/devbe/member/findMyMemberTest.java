package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저정보 조회 테스트")
public class findMyMemberTest extends DevBeApplicationTests {

    String url = "/api/members/my";

    @Test
    @DisplayName("나의 유저정보 조회")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
    public void findMyTest() throws Exception {

        //when,then
        mockMvc.perform(get(url))
                .andExpect(jsonPath("mail").value("test@test1.ac.kr"))
                .andDo(print());
    }

    @Test
    @DisplayName("나의 유저정보 조회 실패: 인증되지 않은 사용자")
    public void findMyMemberFailTest() throws Exception{

        //when,then
        mockMvc.perform(get(url))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
