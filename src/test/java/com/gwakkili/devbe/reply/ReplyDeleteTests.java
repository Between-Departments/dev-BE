package com.gwakkili.devbe.reply;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("댓글 삭제 테스트")
@Transactional
public class ReplyDeleteTests extends DevBeApplicationTests {

    String url = "/api/replies/{replyId}";

    @DisplayName("성공: 댓글을 쓴 멤버")
    @Test
    @WithMockMember(memberId = 1, roles = "ROLE_USER")
    public void successByUser() throws Exception {

        //given
        long replyId = 1;

        //when, then
        mockMvc.perform(delete(url, replyId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("성공: 관리자")
    @Test
    @WithMockMember(memberId = 3, roles = "ROLE_MANAGER")
    public void successByManager() throws Exception {

        //given
        long replyId = 1;

        //when, then
        mockMvc.perform(delete(url, replyId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("실패: 권한 없음")
    @Test
    @WithMockMember(memberId = 5, roles = "ROLE_USER")
    public void failByAccessDenied() throws Exception {

        //given
        long replyId = 1;

        //when, then
        mockMvc.perform(delete(url, replyId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }
}
