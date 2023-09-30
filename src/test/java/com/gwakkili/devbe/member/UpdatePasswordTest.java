package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.member.dto.UpdatePasswordDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("비밀번호 변경 테스트")
public class UpdatePasswordTest extends DevBeApplicationTests {

    String url = "/members/my/password";

    @Test
    @DisplayName("비밀번호 변경 성공")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
    public void updatePasswordSuccessTest() throws Exception {
        //given
        UpdatePasswordDto updatePasswordDto = UpdatePasswordDto.builder()
                .newPassword("b45674567!")
                .newPasswordConfirm("b45674567!")
                .oldPassword("a12341234!")
                .build();
        String content = objectMapper.writeValueAsString(updatePasswordDto);
        //when, then
        mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호 변경 실패: 유효성 검사 실패")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
    public void updatePasswordFailByInvalidTest() throws Exception {
        //given
        UpdatePasswordDto updatePasswordDto = UpdatePasswordDto.builder()
                .newPassword("1")
                .newPasswordConfirm("b45674568!")
                .oldPassword("a123451234!")
                .build();
        String content = objectMapper.writeValueAsString(updatePasswordDto);
        //when, then
        mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호 변경 실패: 인증되지 않은 사용자")
    @WithAnonymousUser
    public void updatePasswordFailByUnauthorizedTest() throws Exception{
        //given
        UpdatePasswordDto updatePasswordDto = UpdatePasswordDto.builder()
                .newPassword("b45674567!")
                .newPasswordConfirm("b45674567!")
                .oldPassword("a12341234!")
                .build();

        String content = objectMapper.writeValueAsString(updatePasswordDto);
        //when, then
        mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }
}
