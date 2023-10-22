package com.gwakkili.devbe.post;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.dto.request.FreePostSaveDto;
import com.gwakkili.devbe.post.dto.request.NeedHelpPostSaveDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.util.WithMockMember;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PostSaveTests extends DevBeApplicationTests {

    String freePostUrl = "/api/posts/free";
    String needHelpPostUrl = "/api/posts/needhelp";
    List<String> imageUrls = new ArrayList<>();

    @BeforeEach
    public void setImages(){
        imageUrls.add("test_image1_url");
        imageUrls.add("test_image2_url");
        imageUrls.add("test_image3_url");
    }

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success() throws Exception{

        FreePostSaveDto freePostSaveDto = FreePostSaveDto.builder()
                .title("title1")
                .content("content1")
                .tag(Post.Tag.TOGETHER)
                .imageUrls(imageUrls)
                .isAnonymous(true)
                .build();


        String content1 = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(freePostSaveDto.getTitle()))
                .andExpect(jsonPath("content").value(freePostSaveDto.getContent()))
                .andExpect(jsonPath("isAnonymous").value(freePostSaveDto.getIsAnonymous()))
                .andExpect(jsonPath("writer.memberId").isEmpty())
                .andExpect(jsonPath("writer.nickname").value("익명"))
                .andDo(print());

        System.out.println("=============================================================================================================================================================================================");

        NeedHelpPostSaveDto needHelpPostSaveDto = NeedHelpPostSaveDto.builder()
                .title("title1")
                .content("content1")
                .majorCategory(Major.Category.ENGINEERING)
//                .imageUrls(imageUrls)
                .isAnonymous(false)
                .build();


        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(needHelpPostSaveDto.getTitle()))
                .andExpect(jsonPath("content").value(needHelpPostSaveDto.getContent()))
                .andExpect(jsonPath("isAnonymous").value(needHelpPostSaveDto.getIsAnonymous()))
                .andExpect(jsonPath("writer.memberId").value(1))
                .andExpect(jsonPath("writer.nickname").value("테스트멤버"))
                .andDo(print());

    }

    @Test
    @DisplayName("실패: 데이터 길이 검증 실패")
    @WithMockMember
    public void failByInvalidRequestDataLength() throws Exception {
        String titleLowerBound = RandomStringUtils.random(2, true, true);
        String titleUpperBound = RandomStringUtils.random(31, true, true);
        String contentLowerBound =RandomStringUtils.random(4, true, true);
        String contentUpperBound =RandomStringUtils.random(1001, true, true);

        FreePostSaveDto freePostSaveDto = FreePostSaveDto.builder()
                .title(titleLowerBound)
                .content(contentLowerBound)
                .tag(Post.Tag.TOGETHER)
                .imageUrls(imageUrls)
                .isAnonymous(true)
                .build();


        String content1 = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isBadRequest())
                .andDo(print());

        System.out.println("=============================================================================================================================================================================================");

        NeedHelpPostSaveDto needHelpPostSaveDto = NeedHelpPostSaveDto.builder()
                .title(titleUpperBound)
                .content(contentUpperBound)
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls)
                .isAnonymous(false)
                .build();

        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("실패: Enum 검증 실패")
    @WithMockMember
    public void failByInvalidRequestDataEnum() throws Exception {

        Map<String, Object> freePostSaveDto = new HashMap<>();

        // * 값 자체가 요청 데이터에 없는 경우
        freePostSaveDto.put("title","title1");
        freePostSaveDto.put("content","content1");
//        freePostSaveDto.put("tag","");
        freePostSaveDto.put("imageUrls", imageUrls);
        freePostSaveDto.put("isAnonymous","true");

        String content = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andDo(print());

        Map<String, Object> freePostSaveDto1 = new HashMap<>();

        // * 값이 빈 값("", "   ", 등)으로 오는 경우
        freePostSaveDto1.put("title","title1");
        freePostSaveDto1.put("content","content1");
        freePostSaveDto1.put("tag"," ");
        freePostSaveDto1.put("imageUrls", imageUrls);
        freePostSaveDto1.put("isAnonymous","true");

        String content1 = objectMapper.writeValueAsString(freePostSaveDto1);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isBadRequest())
                .andDo(print());


        Map<String, Object> needHelpPostSaveDto = new HashMap<>();

        // * 틀린 값이 오는 경우
        needHelpPostSaveDto.put("title","title1");
        needHelpPostSaveDto.put("content","content1");
        needHelpPostSaveDto.put("majorCategory","WRONG");
        needHelpPostSaveDto.put("imageUrls", imageUrls);
        needHelpPostSaveDto.put("isAnonymous","false");

        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("실패: isAnonymous NotNull 검증 실패")
    @WithMockMember
    public void failByInvalidAnonymous() throws Exception {

        FreePostSaveDto freePostSaveDto = FreePostSaveDto.builder()
                .title("title1")
                .content("content1")
                .tag(Post.Tag.TOGETHER)
                .imageUrls(imageUrls)
                .build();


        String content1 = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isBadRequest())
                .andDo(print());

        System.out.println("=============================================================================================================================================================================================");

        NeedHelpPostSaveDto needHelpPostSaveDto = NeedHelpPostSaveDto.builder()
                .title("title1")
                .content("content1")
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls)
                .build();


        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
