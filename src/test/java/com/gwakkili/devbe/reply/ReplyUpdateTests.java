package com.gwakkili.devbe.reply;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.reply.dto.request.ReplyUpdateDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("댓글 수정 테스트")
@Transactional
public class ReplyUpdateTests extends DevBeApplicationTests {

    String url = "/api/replies/{replyId}";

    @DisplayName("성공")
    @Test
    @WithMockMember
    public void success() throws Exception {

        //given
        long replyId = 1;
        ReplyUpdateDto replyUpdateDto = ReplyUpdateDto.builder().content("update").build();
        String content = objectMapper.writeValueAsString(replyUpdateDto);

        //when, then
        mockMvc.perform(patch(url, replyId).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").value(replyUpdateDto.getContent()))
                .andDo(print());
    }

    @DisplayName("실패: 권한 없음")
    @Test
    @WithMockMember(memberId = 5)
    public void failByAccessDenied() throws Exception {
        long replyId = 1;
        ReplyUpdateDto replyUpdateDto = ReplyUpdateDto.builder().content("update").build();
        String content = objectMapper.writeValueAsString(replyUpdateDto);

        //when, then
        mockMvc.perform(patch(url, replyId).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }

    @DisplayName("실패: 유효성 검증 실패")
    @Test
    @WithMockMember
    public void failByInvalid() throws Exception {
        long replyId = 1;
        String str = IntStream.rangeClosed(1, 100).mapToObj(String::valueOf).collect(Collectors.joining());
        ReplyUpdateDto replyUpdateDto = ReplyUpdateDto.builder().content(str).build();
        String content = objectMapper.writeValueAsString(replyUpdateDto);

        //when, then
        mockMvc.perform(patch(url, replyId).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(ExceptionCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("fieldErrors.content").exists())
                .andDo(print());
    }
}
