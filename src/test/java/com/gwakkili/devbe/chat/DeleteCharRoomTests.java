package com.gwakkili.devbe.chat;

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

@DisplayName("채팅방 삭제 테스트")
@Transactional
public class DeleteCharRoomTests extends DevBeApplicationTests {

    private String url = "/api/chat/rooms/{roomId}";

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success() throws Exception {
        //given
        long roomId = 1;

        //when, then
        mockMvc.perform(delete(url, roomId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 자신이 속하지 않은 채팅방 삭제")
    @WithMockMember(memberId = 5)
    public void failByAccessDined() throws Exception {
        //given
        long roomId = 1;

        //when, then
        mockMvc.perform(delete(url, roomId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }
}
