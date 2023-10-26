package com.gwakkili.devbe.chat;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("채팅 메세지 목록 조회 테스트")
public class GetChatMessageListTests extends DevBeApplicationTests {

    private String url = "/api/chat/rooms/{roomId}/messages";

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success() throws Exception {
        //given
        long roomId = 1;
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("page", "1");
        params.add("size", "10");

        //when, then
        mockMvc.perform(get(url, roomId).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("dataList.length()").value(10))
                .andExpect(jsonPath("dataList[*].chatRoomId").value(everyItem(equalTo(roomId))))
                .andExpect(jsonPath("dataList[*].sender").value(everyItem(in(List.of("테스트멤버", "테스트멤버1")))))
                .andExpect(jsonPath("dataList[*].content").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 속해 있지 않은 채팅방 메시지 요청")
    @WithMockMember(memberId = 3)
    public void failByAccessDined() throws Exception {
        //given
        long roomId = 1;
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("page", "1");
        params.add("size", "10");

        //when, then
        mockMvc.perform(get(url, roomId).params(params))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 채팅 메시지를 찾을 수 없음")
    @WithMockMember(memberId = 1)
    public void failByNotFound() throws Exception {
        //given
        long roomId = 10;
        MultiValueMap params = new LinkedMultiValueMap();
        params.add("page", "1");
        params.add("size", "10");

        //when, then
        mockMvc.perform(get(url, roomId).params(params))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_CHAT_MESSAGE.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.NOT_FOUND_CHAT_MESSAGE.getMessage()))
                .andDo(print());
    }
}
