package com.gwakkili.devbe.member;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.image.dto.ImageDto;
import com.gwakkili.devbe.member.dto.NicknameAndImageDto;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("닉네임 & 이미지 변경 테스트")
public class updateNicknameAndImageTest extends DevBeApplicationTests {

    String url = "/api/members/my/image";

    @Test
    @DisplayName("닉네임 & 이미지 변경 성공")
    @WithMockMember(mail = "test@test1.ac.kr", password = "a12341234!")
    public void success() throws Exception {
        NicknameAndImageDto nicknameAndImageDto = NicknameAndImageDto.builder()
                .nickname("name2")
                .image(ImageDto.builder().imageUrl("http://images/image1.jpg").thumbnailUrl("http://images/image2.jpg").build())
                .build();
        String content = objectMapper.writeValueAsString(nicknameAndImageDto);
        mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value(nicknameAndImageDto.getNickname()))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 & 이미지 변경 실패: 인증되지 않은 사용자")
    @WithAnonymousUser
    public void fail() throws Exception{
        NicknameAndImageDto nicknameAndImageDto = NicknameAndImageDto.builder()
                .nickname("name2")
                .image(ImageDto.builder().imageUrl("http://images/image1.jpg").thumbnailUrl("http://images/image2.jpg").build())
                .build();
        String content = objectMapper.writeValueAsString(nicknameAndImageDto);
        mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
