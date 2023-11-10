package com.gwakkili.devbe.post;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시물 삭제 테스트")
@Transactional
public class PostDeleteTests extends DevBeApplicationTests {

    String url = "/api/posts/{postId}";

    @Test
    @DisplayName("성공")
    @WithMockMember
    @Commit
    public void success() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete(url, postId))
                .andExpect(status().isNoContent())
                .andDo(print());

        // ! @TransactionalEventListener의 코드 동작을 확인하기 위해 @Commit으로 설정
    }

    @Test
    @DisplayName("관리자 삭제 성공")
    @WithMockMember(roles = {"ROLE_MANAGER"})
    @Commit
    public void successByManager() throws Exception {
        Long postId = 2L;

        mockMvc.perform(delete(url, postId))
                .andExpect(status().isNoContent())
                .andDo(print());

        // ! 정상적으로 member의 신고누적수가 올라갔는지 확인 필요 -> 디버깅으로 확인하긴 했으나 코드적으로 검증가능한가?
        // ! @TransactionalEventListener의 코드 동작을 확인하기 위해 @Commit으로 설정

    }

    @DisplayName("실패: 권한 없음")
    @Test
    @WithMockMember(memberId = 5)
    public void failByAccessDenied() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete(url, postId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }
}
