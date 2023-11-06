package com.gwakkili.devbe.chat;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("채팅방 목록 조회 테스트")
public class GetChatRoomListTests extends DevBeApplicationTests {

    String url = "/api/chat/rooms";

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success() throws Exception {

        //when, then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size").value(9))
                .andExpect(jsonPath("dataList[*].chatRoomId").exists())
                .andExpect(jsonPath("dataList[*].member.memberId").value(not(contains(1))))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 채팅방을 찾을 수 없음")
    @WithMockMember(memberId = 11)
    public void failByNotFoundChatRoom() throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_CHAT_ROOM.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.NOT_FOUND_CHAT_ROOM.getMessage()))
                .andDo(print());
    }


}
