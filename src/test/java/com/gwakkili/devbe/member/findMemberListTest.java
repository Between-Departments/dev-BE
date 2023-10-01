package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 목록조회 테스트")
public class findMemberListTest extends DevBeApplicationTests {

    String url = "/api/members";

    @Test
    @DisplayName("유저 목록조회 성공")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!", roles = "ROLE_MANAGER")
    public void findMemberListTest() throws Exception{

        //when,then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(10))
                .andDo(print());
    }

    @Test
    @DisplayName("유저 목록조회 성공: 키워드 검색")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!", roles = "ROLE_MANAGER")
    public void findMemberListByKeywordTest() throws Exception{

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "20");
        params.add("keyword", "test1.ac.kr");

        //when,then
        mockMvc.perform(get(url).params(params))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("유저 목록조회 실패: 권한이 없는 사용자")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!", roles = "ROLE_USER")
    public void findMemberListByForbidden() throws Exception {

        //when,then
        mockMvc.perform(get(url))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

}
