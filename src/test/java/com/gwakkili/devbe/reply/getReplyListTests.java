package com.gwakkili.devbe.reply;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("댓글 목록 조회 테스트")
public class getReplyListTests extends DevBeApplicationTests {

    @Nested
    @DisplayName("댓글 목록 조회 테스트")
    class getReplyListTest {

        String url = "/api/posts/{postId}/replies";

        @DisplayName("성공")
        @Test
        public void success() throws Exception {
            //given
            int postId = 1;

            //when, then
            mockMvc.perform(get(url, postId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList[*].postId").value(everyItem(equalTo(postId))))
                    .andDo(print());
        }

        @DisplayName("실패: 댓글을 찾을 수 없음")
        @Test
        public void fail() throws Exception {
            //given
            int postId = 1000;

            //when, then
            mockMvc.perform(get(url, postId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_REPLY.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.NOT_FOUND_REPLY.getMessage()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("나의 댓글 목록 조회 테스트")
    class getMyReplyListTest {

        String url = "/api/replies/my";

        @Test
        @DisplayName("성공")
        @WithMockMember
        public void success() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList[*].writer.nickname").value(everyItem(equalTo("테스트멤버"))))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 인증되지 않은 사용자")
        public void failByUnauthorized() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("code").value(ExceptionCode.UNAUTHORIZED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.UNAUTHORIZED.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 댓글을 찾을 수 없음")
        @WithMockMember(memberId = 100)
        public void failByNotFound() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_REPLY.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.NOT_FOUND_REPLY.getMessage()))
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("신고된 댓글 목록 조회 테스트")
    class getReportedReplyListTest {

        String url = "/api/replies/report";

        @Test
        @DisplayName("성공")
        @WithMockMember(roles = "ROLE_MANAGER")
        public void success() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList[*].reportCount").value(everyItem(greaterThan(0))))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 권한없음")
        @WithMockMember(roles = "ROLE_USER")
        public void failByAccessDenied() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                    .andDo(print());
        }

    }
}
