package com.gwakkili.devbe.chat;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.chat.dto.Request.SaveChatRoomDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("채팅방 생성 테스트")
@Transactional
public class SaveChatRoomTests extends DevBeApplicationTests {

    String url = "/api/chat/rooms";

    @Test
    @DisplayName("성공")
    @WithMockMember(memberId = 5)
    public void success() throws Exception {
        //given
        long memberId = 2;
        SaveChatRoomDto saveChatRoomDto = new SaveChatRoomDto(memberId);

        String content = objectMapper.writeValueAsString(saveChatRoomDto);

        //when, then
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("chatRoomId").exists())
                .andExpect(jsonPath("member.memberId").value(memberId))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 채팅방이 이미 존재")
    @WithMockMember(memberId = 1)
    public void failByDuplicateChatRoom() throws Exception {
        //given
        long memberId = 2;
        SaveChatRoomDto saveChatRoomDto = new SaveChatRoomDto(memberId);

        String content = objectMapper.writeValueAsString(saveChatRoomDto);

        //when, then
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("code").value(ExceptionCode.DUPLICATE_CHAT_ROOM.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.DUPLICATE_CHAT_ROOM.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 회원을 찾을 수 없음")
    @WithMockMember
    public void failByNotFoundMember() throws Exception {
        //given
        long memberId = 1000;
        SaveChatRoomDto saveChatRoomDto = new SaveChatRoomDto(memberId);

        String content = objectMapper.writeValueAsString(saveChatRoomDto);

        //when, then
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value(ExceptionCode.NOT_FOUND_MEMBER.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.NOT_FOUND_MEMBER.getMessage()))
                .andDo(print());
    }

}
