package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.mail.entity.MailAuthCode;
import com.gwakkili.devbe.mail.repository.MailAuthCodeRepository;
import com.gwakkili.devbe.member.dto.request.UpdateMajorDto;
import com.gwakkili.devbe.member.dto.request.UpdateNicknameAndImageDto;
import com.gwakkili.devbe.member.dto.request.UpdatePasswordDto;
import com.gwakkili.devbe.member.dto.request.UpdateSchoolDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 변경 테스트")
@WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
@Transactional
public class UpdateMemberTest extends DevBeApplicationTests {

    @Autowired
    private MailAuthCodeRepository mailAuthCodeRepository;

    @Nested
    @DisplayName("비밀번호 변경 테스트")
    class UpdatePasswordTest {
        String url = "/api/members/my/password";

        @Test
        @DisplayName("변경 성공")
        public void success() throws Exception {
            //given
            UpdatePasswordDto updatePasswordDto = UpdatePasswordDto.builder()
                    .newPassword("b45674567!")
                    .newPasswordConfirm("b45674567!")
                    .password("a12341234!")
                    .build();
            String content = objectMapper.writeValueAsString(updatePasswordDto);
            //when, then
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("변경 실패: 유효성 검사 실패")
        public void failByInvalidInput() throws Exception {
            //given
            UpdatePasswordDto updatePasswordDto = UpdatePasswordDto.builder()
                    .newPassword("1")
                    .newPasswordConfirm("b45674568!")
                    .password("a123451234!")
                    .build();
            String content = objectMapper.writeValueAsString(updatePasswordDto);
            //when, then
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("code").value(ExceptionCode.INVALID_INPUT_VALUE.getCode()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("닉네임 및 이미지 변경 테스트")
    class UpdateNicknameAndImageTest {

        String url = "/api/members/my/image";

        @Test
        @DisplayName("변경 성공")
        public void success() throws Exception {
            UpdateNicknameAndImageDto updateNicknameAndImageDto = UpdateNicknameAndImageDto.builder()
                    .nickname("name2")
                    .imageUrl("http://images/image1.jpg")
                    .build();
            String content = objectMapper.writeValueAsString(updateNicknameAndImageDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("nickname").value(updateNicknameAndImageDto.getNickname()))
                    .andDo(print());
        }

        @Test
        @DisplayName("변경 실퍠: 유효성 검사 실패")
        public void failByInvalidTest() throws Exception {
            UpdateNicknameAndImageDto updateNicknameAndImageDto = UpdateNicknameAndImageDto.builder()
                    .nickname("테스트멤버")
                    .imageUrl("rrere")
                    .build();
            String content = objectMapper.writeValueAsString(updateNicknameAndImageDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("code").value(ExceptionCode.INVALID_INPUT_VALUE.getCode()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("학교 정보 변경 테스트")
    class UpdateSchoolTest {

        String url = "/api/members/my/school";

        private void saveMailAuthCode(String mail) {
            MailAuthCode mailAuthCode = MailAuthCode.builder()
                    .authCode("111111")
                    .mail(mail)
                    .Auth(true)
                    .build();
            mailAuthCodeRepository.save(mailAuthCode);
        }

        @Test
        @DisplayName("변경 성공")
        public void success() throws Exception {
            //given
            String newMail = "testUdate@test3.ac.kr";
            UpdateSchoolDto updateSchoolDto = UpdateSchoolDto.builder()
                    .newMail(newMail)
                    .school("테스트대학3")
                    .build();
            saveMailAuthCode(newMail);
            //when, then
            String content = objectMapper.writeValueAsString(updateSchoolDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("변경 실퍠: 유효성 검사 실패")
        public void failByInvalidTest() throws Exception {
            String newMail = "test3@test1.ac.kr";
            UpdateSchoolDto updateSchoolDto = UpdateSchoolDto.builder()
                    .newMail(newMail)
                    .school("없는대학")
                    .build();
            String content = objectMapper.writeValueAsString(updateSchoolDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("code").value(ExceptionCode.INVALID_INPUT_VALUE.getCode()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("학과 정보 변경 테스트")
    class UpdateMajorTest {

        String url = "/api/members/my/major";

        @Test
        @DisplayName("변경 성공")
        public void success() throws Exception {
            UpdateMajorDto updateMajorDto = UpdateMajorDto.builder().major("테스트학과1").build();
            String content = objectMapper.writeValueAsString(updateMajorDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("변경 실퍠: 유효성 검사 실패")
        @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
        public void failByInvalidTest() throws Exception {
            UpdateMajorDto updateMajorDto = UpdateMajorDto.builder().major("없는학과").build();
            String content = objectMapper.writeValueAsString(updateMajorDto);
            mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("code").value(ExceptionCode.INVALID_INPUT_VALUE.getCode()))
                    .andDo(print());
        }
    }
}
