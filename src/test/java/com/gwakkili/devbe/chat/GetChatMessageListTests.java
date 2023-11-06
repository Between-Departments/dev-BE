package com.gwakkili.devbe.chat;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    private final String url = "/api/chat/rooms/{roomId}/messages";

    @DisplayName("성공")
    @WithMockMember
    @ParameterizedTest
    @CsvSource(value = {"1:10", "2:10", "3:10", "4:0"}, delimiter = ':')
    public void success(String page, int length) throws Exception {
        //given
        long roomId = 1;
        String masterNickname = "테스트멤버";
        String memberNickname = "테스트멤버1";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", page);
        params.add("size", "10");

        //when, then
        mockMvc.perform(get(url, roomId).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("dataList.length()").value(length))
                .andExpect(jsonPath("dataList[*].chatRoomId").value(everyItem(equalTo(roomId))))
                .andExpect(jsonPath("dataList[*].sender").value(everyItem(in(List.of(masterNickname, memberNickname)))))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 속해 있지 않은 채팅방 메시지 요청")
    @WithMockMember(memberId = 3)
    public void failByAccessDined() throws Exception {
        //given
        long roomId = 1;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "10");

        //when, then
        mockMvc.perform(get(url, roomId).params(params))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }


}
