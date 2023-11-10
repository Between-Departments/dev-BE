package com.gwakkili.devbe.report;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.report.dto.request.PostReportSaveDto;
import com.gwakkili.devbe.report.entity.Report;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시물 신고 테스트")
@Transactional
public class PostReportTests extends DevBeApplicationTests {

    @Nested
    @DisplayName("특정 게시물 신고")
    class 신고{
        String url = "/api/posts/{postId}/report";

        PostReportSaveDto postReportSaveDto = PostReportSaveDto.builder()
                .type(Report.Type.DISCOMFORT)
                .content("테스트용 신고 샘플입니다.")
                .build();


        @Test
        @DisplayName("성공")
        @WithMockMember
        public void success() throws Exception {
            Long postId = 30L;

            String content = objectMapper.writeValueAsString(postReportSaveDto);

            mockMvc.perform(post(url,postId).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 인증 정보 없음")
        public void failByUnauthorized() throws Exception {
            Long postId = 1L;

            String content = objectMapper.writeValueAsString(postReportSaveDto);
            mockMvc.perform(post(url,postId).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("code").value(ExceptionCode.UNAUTHORIZED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.UNAUTHORIZED.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 중복 신고")
        @WithMockMember
        public void failByDuplicateReport() throws Exception {
            Long postId = 1L;

            String content = objectMapper.writeValueAsString(postReportSaveDto);
            mockMvc.perform(post(url,postId).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("code").value(ExceptionCode.DUPLICATE_REPORT.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.DUPLICATE_REPORT.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 : 신고하려는 게시물이 없는 경우")
        @WithMockMember
        public void noResult() throws Exception {
            Long postId = 1000L;

            String content = objectMapper.writeValueAsString(postReportSaveDto);
            mockMvc.perform(post(url,postId).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_POST.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.NOT_FOUND_POST.getMessage()))
                    .andDo(print());
        }

    }


    @Nested
    @DisplayName("신고 내용 조회")
    class 조회{
        String url = "/api/posts/{postId}/report";

        @Test
        @DisplayName("성공")
        @WithMockMember(roles = {"ROLE_MANAGER"})
        public void success() throws Exception {
            Long postId = 1L;

            mockMvc.perform(get(url,postId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList",hasSize(10)))
                    .andExpect(jsonPath("dataList[*].postId").value(everyItem(equalTo(1))))
                    .andExpect(jsonPath("hasNext").value(Boolean.TRUE))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 권한 없음")
        @WithMockMember
        public void failByAccessDenied() throws Exception {
            Long postId = 1L;

            mockMvc.perform(get(url,postId))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("데이터 없음 : 신고가 없는 게시물을 조회하는 경우")
        @WithMockMember(roles = {"ROLE_MANAGER"})
        public void noResult() throws Exception {
            Long postId = 100L;

            mockMvc.perform(get(url,postId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("dataList").exists())
                    .andExpect(jsonPath("dataList").isArray())
                    .andExpect(jsonPath("dataList").isEmpty())
                    .andExpect(jsonPath("hasNext").value(Boolean.FALSE))
                    .andDo(print());
        }

    }


    @Nested
    @DisplayName("신고 내용 삭제")
    class 삭제{
        String url = "/api/posts/{postId}/report/{reportId}";

        @Test
        @DisplayName("성공")
        @WithMockMember(roles = {"ROLE_MANAGER"})
        public void success() throws Exception {
            Long postId = 1L;
            Long reportId = 1L;

            mockMvc.perform(delete(url,postId,reportId))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("실패: 권한 없음")
        @WithMockMember
        public void failByAccessDenied() throws Exception {
            Long postId = 1L;
            Long reportId = 1L;

            mockMvc.perform(delete(url,postId,reportId))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                    .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 : 삭제하려는 특정 신고 데이터가 없는 경우")
        @WithMockMember(roles = {"ROLE_MANAGER"})
        public void noResult() throws Exception {
            Long postId = 1L;
            Long reportId = 1000L;

            // ! reportId가 진짜 postId의 report인지 확인 안함
            // ! 근데 굳이... 어드민 용인데? 해야할까

            mockMvc.perform(delete(url,postId,reportId))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

    }
}
