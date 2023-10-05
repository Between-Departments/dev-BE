package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.member.dto.NicknameAndImageDto;
import com.gwakkili.devbe.member.dto.UpdatePasswordDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 변경 테스트")
public class UpdateMemberTest extends DevBeApplicationTests {

    @Nested
    @DisplayName("비밀번호 변경 테스트")
    class UpdatePasswordTest {
        String url = "/api/members/my/password";

        @Test
        @DisplayName("비밀번호 변경 성공")
        @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
        public void success() throws Exception {
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
        public void failByInvalidInput() throws Exception {
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
    }

    @Nested
    @DisplayName("닉네임 및 이미지 변경 테스트")
    class UpdateNicknameAndImageTest {

        String url = "/api/members/my/image";

        @Test
        @DisplayName("닉네임 & 이미지 변경 성공")
        @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
        public void success() throws Exception {
            NicknameAndImageDto nicknameAndImageDto = NicknameAndImageDto.builder()
                    .nickname("name2")
                    .imageUrl("http://images/image1.jpg")
                    .build();
            String content = objectMapper.writeValueAsString(nicknameAndImageDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("nickname").value(nicknameAndImageDto.getNickname()))
                    .andDo(print());
        }

        @Test
        @DisplayName("실퍠: 유효성 검사 실패")
        @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
        public void updateNicknameAndImageFailByInvalidTest() throws Exception {
            NicknameAndImageDto nicknameAndImageDto = NicknameAndImageDto.builder()
                    .nickname("테스트멤버")
                    .imageUrl("rrere")
                    .build();
            String content = objectMapper.writeValueAsString(nicknameAndImageDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("fieldErrors.nickname").exists())
                    .andExpect(jsonPath("fieldErrors.imageUrl").exists())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("닉네임 및 이미지 변경 테스트")
    class UpdateSchoolTest {

        String url = "/api/members/my/image";

        @Test
        @DisplayName("")
        @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
        public void success() throws Exception {
            NicknameAndImageDto nicknameAndImageDto = NicknameAndImageDto.builder()
                    .nickname("name2")
                    .imageUrl("http://images/image1.jpg")
                    .build();
            String content = objectMapper.writeValueAsString(nicknameAndImageDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("nickname").value(nicknameAndImageDto.getNickname()))
                    .andDo(print());
        }

        @Test
        @DisplayName("닉네임 & 이미지 변경 실퍠: 유효성 검사 실패")
        @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
        public void updateNicknameAndImageFailByInvalidTest() throws Exception {
            NicknameAndImageDto nicknameAndImageDto = NicknameAndImageDto.builder()
                    .nickname("테스트멤버")
                    .imageUrl("rrere")
                    .build();
            String content = objectMapper.writeValueAsString(nicknameAndImageDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("fieldErrors.nickname").exists())
                    .andExpect(jsonPath("fieldErrors.imageUrl").exists())
                    .andDo(print());
        }
    }
}
