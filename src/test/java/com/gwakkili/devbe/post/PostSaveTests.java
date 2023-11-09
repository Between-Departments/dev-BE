package com.gwakkili.devbe.post;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.dto.request.FreePostSaveDto;
import com.gwakkili.devbe.post.dto.request.NeedHelpPostSaveDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.util.WithMockMember;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시물 저장 테스트")
@Transactional
public class PostSaveTests extends DevBeApplicationTests {

    String freePostUrl = "/api/posts/free";
    String needHelpPostUrl = "/api/posts/needhelp";

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success() throws Exception{
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://test.com/test_image1_url");
        imageUrls.add("http://test.com/test_image2_url");
        imageUrls.add("http://test.com/test_image3_url");

        // ! 익명 게시글
        FreePostSaveDto freePostSaveDto = FreePostSaveDto.builder()
                .title("제목은 최소 2글자, 최대 30글자 입니다.")
                .content("본문 내용은 최소 10글자, 최대 1000글자 입니다.")
                .tag(Post.Tag.TOGETHER)
                .imageUrls(imageUrls)
                .isAnonymous(true)
                .build();

        String content1 = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(freePostSaveDto.getTitle()))
                .andExpect(jsonPath("content").value(freePostSaveDto.getContent()))
                .andExpect(jsonPath("boardType").value(Post.BoardType.FREE.name()))
                .andExpect(jsonPath("tag").value(freePostSaveDto.getTag().name()))
                .andExpect(jsonPath("images").isArray())
                .andExpect(jsonPath("images",hasSize(3)))
                .andExpect(jsonPath("isAnonymous").value(freePostSaveDto.getIsAnonymous()))
                .andExpect(jsonPath("writer.memberId").doesNotExist())
                .andExpect(jsonPath("writer.nickname").value("익명"))
                .andExpect(jsonPath("writer.school").doesNotExist())
                .andExpect(jsonPath("writer.major").doesNotExist())
                .andExpect(jsonPath("isMine").value(Boolean.TRUE))
                .andExpect(jsonPath("isBookmarked").value(Boolean.FALSE))
                .andExpect(jsonPath("isRecommended").value(Boolean.FALSE))
                .andDo(print());

        System.out.println("=============================================================================================================================================================================================");

        NeedHelpPostSaveDto needHelpPostSaveDto = NeedHelpPostSaveDto.builder()
                .title("제목은 최소 2글자, 최대 30글자 입니다.")
                .content("본문 내용은 최소 10글자, 최대 1000글자 입니다.")
                .majorCategory(Major.Category.ENGINEERING)
                .isAnonymous(false)
                .build();

        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(needHelpPostSaveDto.getTitle()))
                .andExpect(jsonPath("content").value(needHelpPostSaveDto.getContent()))
                .andExpect(jsonPath("boardType").value(Post.BoardType.NEED_HELP.name()))
                .andExpect(jsonPath("majorCategory").value(needHelpPostSaveDto.getMajorCategory().name()))
                .andExpect(jsonPath("images").doesNotExist())
                .andExpect(jsonPath("isAnonymous").value(needHelpPostSaveDto.getIsAnonymous()))
                .andExpect(jsonPath("writer.memberId").value(1))
                .andExpect(jsonPath("writer.nickname").value("테스트멤버"))
                .andExpect(jsonPath("writer.school").value("테스트대학1"))
                .andExpect(jsonPath("writer.major").value("테스트학과1"))
                .andExpect(jsonPath("isMine").value(Boolean.TRUE))
                .andExpect(jsonPath("isBookmarked").value(Boolean.FALSE))
                .andExpect(jsonPath("isRecommended").value(Boolean.FALSE))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: 제목, 본문 데이터 길이 검증 실패")
    @WithMockMember
    public void failByInvalidDataLength() throws Exception {
        List<String> imageUrls = new ArrayList<>();

        String titleLowerBound = RandomStringUtils.random(1, true, true);
        String titleUpperBound = RandomStringUtils.random(31, true, true);
        String contentLowerBound =RandomStringUtils.random(1, true, true);
        String contentUpperBound =RandomStringUtils.random(1001, true, true);

        FreePostSaveDto titleLowerBoundPost = FreePostSaveDto.builder()
                .title(titleLowerBound)
                .content("본문 데이터 길이는 정상입니다.")
                .tag(Post.Tag.TOGETHER)
                .imageUrls(imageUrls)
                .isAnonymous(true)
                .build();


        String content1 = objectMapper.writeValueAsString(titleLowerBoundPost);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andExpect(jsonPath("fieldErrors.title").exists())
                .andExpect(jsonPath("fieldErrors.content").doesNotExist())
                .andDo(print());

        System.out.println("=============================================================================================================================================================================================");

        NeedHelpPostSaveDto contentUpperBoundPost = NeedHelpPostSaveDto.builder()
                .title("제목 데이터 길이는 정상입니다.")
                .content(contentUpperBound)
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls)
                .isAnonymous(false)
                .build();

        String content2 = objectMapper.writeValueAsString(contentUpperBoundPost);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andExpect(jsonPath("fieldErrors.title").doesNotExist())
                .andExpect(jsonPath("fieldErrors.content").exists())
                .andDo(print());


        System.out.println("=============================================================================================================================================================================================");

        NeedHelpPostSaveDto allWrong = NeedHelpPostSaveDto.builder()
                .title(titleUpperBound)
                .content(contentLowerBound)
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls)
                .isAnonymous(false)
                .build();

        String content3 = objectMapper.writeValueAsString(allWrong);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content3))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(2)))
                .andExpect(jsonPath("fieldErrors.title").exists())
                .andExpect(jsonPath("fieldErrors.content").exists())
                .andDo(print());

    }

    @Test
    @DisplayName("실패: Enum 검증 실패")
    @WithMockMember
    public void failByInvalidEnum() throws Exception {
        Map<String, Object> freePostSaveDto = new HashMap<>();

        // * 값 자체가 요청 데이터에 없는 경우
        freePostSaveDto.put("title","제목 데이터 길이는 정상입니다.");
        freePostSaveDto.put("content","본문 데이터 길이는 정상입니다.");
//        freePostSaveDto.put("tag","");
        freePostSaveDto.put("isAnonymous","true");

        String content = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.tag").exists())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andDo(print());

        Map<String, Object> freePostSaveDto1 = new HashMap<>();

        // * 값이 빈 값("", "   ", 등)으로 오는 경우
        freePostSaveDto1.put("title","제목 데이터 길이는 정상입니다.");
        freePostSaveDto1.put("content","본문 데이터 길이는 정상입니다.");
        freePostSaveDto1.put("tag"," ");
        freePostSaveDto1.put("isAnonymous","true");

        String content1 = objectMapper.writeValueAsString(freePostSaveDto1);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.tag").exists())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andDo(print());


        Map<String, Object> needHelpPostSaveDto = new HashMap<>();

        // * 틀린 값이 오는 경우
        needHelpPostSaveDto.put("title","제목 데이터 길이는 정상입니다.");
        needHelpPostSaveDto.put("content","본문 데이터 길이는 정상입니다.");
        needHelpPostSaveDto.put("majorCategory","WRONG");
        needHelpPostSaveDto.put("isAnonymous","false");

        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.majorCategory").exists())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andDo(print());
    }

    @Test
    @DisplayName("실패: isAnonymous NotNull 검증 실패")
    @WithMockMember
    public void failByInvalidAnonymous() throws Exception {
        List<String> imageUrls = new ArrayList<>();
        FreePostSaveDto freePostSaveDto = FreePostSaveDto.builder()
                .title("제목 데이터 길이는 정상입니다.")
                .content("본문 데이터 길이는 정상입니다.")
                .tag(Post.Tag.TOGETHER)
                .imageUrls(imageUrls)
                .build();


        String content1 = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.isAnonymous").exists())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andDo(print());

        System.out.println("=============================================================================================================================================================================================");

        NeedHelpPostSaveDto needHelpPostSaveDto = NeedHelpPostSaveDto.builder()
                .title("제목 데이터 길이는 정상입니다.")
                .content("본문 데이터 길이는 정상입니다.")
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls)
                .build();


        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.isAnonymous").exists())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andDo(print());
    }


    @Test
    @DisplayName("실패: 이미지 URL 검증 실패")
    @WithMockMember
    public void failByInvalidImageUrls() throws Exception {
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://test.com/test_image1_url");
        imageUrls.add("http://test.com/test_image2_url");
        imageUrls.add("http://test.com/test_image3_url");
        imageUrls.add("http://test.com/test_image4_url");

        FreePostSaveDto freePostSaveDto = FreePostSaveDto.builder()
                .title("제목 데이터 길이는 정상입니다.")
                .content("본문 데이터 길이는 정상입니다.")
                .tag(Post.Tag.TOGETHER)
                .imageUrls(imageUrls)
                .isAnonymous(true)
                .build();

        String content1 = objectMapper.writeValueAsString(freePostSaveDto);
        mockMvc.perform(post(freePostUrl).contentType(MediaType.APPLICATION_JSON).content(content1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors.imageUrls").exists())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(1)))
                .andDo(print());

        System.out.println("=============================================================================================================================================================================================");

        List<String> imageUrls1 = new ArrayList<>();
        imageUrls1.add("wrong_image1_url");
        imageUrls1.add("wrong_image2_url");
        imageUrls1.add("wrong_image3_url");

        NeedHelpPostSaveDto needHelpPostSaveDto = NeedHelpPostSaveDto.builder()
                .title("제목 데이터 길이는 정상입니다.")
                .content("본문 데이터 길이는 정상입니다.")
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls1)
                .isAnonymous(true)
                .build();


        String content2 = objectMapper.writeValueAsString(needHelpPostSaveDto);
        mockMvc.perform(post(needHelpPostUrl).contentType(MediaType.APPLICATION_JSON).content(content2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("fieldErrors",aMapWithSize(3)))
                .andDo(print());
    }
}
