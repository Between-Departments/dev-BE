package com.gwakkili.devbe.reply;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.reply.dto.ReplySaveDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("댓글 저장 테스트")
@Transactional
public class ReplySaveTests extends DevBeApplicationTests {

    String url = "/api/replies";

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success() throws Exception {
        ReplySaveDto replySaveDto = ReplySaveDto.builder()
                .postId(1)
                .content("test1")
                .build();

        String content = objectMapper.writeValueAsString(replySaveDto);
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("content").value(replySaveDto.getContent()))
                .andExpect(jsonPath("postId").value(replySaveDto.getPostId()))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 유효성 검증 실패")
    @WithMockMember
    public void failByInvalid() throws Exception {
        //given
        String str = IntStream.rangeClosed(1, 500).mapToObj(String::valueOf).collect(Collectors.joining());
        System.out.println(str);
        ReplySaveDto replySaveDto = ReplySaveDto.builder()
                .postId(1)
                .content(str)
                .build();

        //when, then
        String content = objectMapper.writeValueAsString(replySaveDto);
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.content").exists())
                .andDo(print());
    }
}
