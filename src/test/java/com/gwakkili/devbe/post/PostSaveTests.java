package com.gwakkili.devbe.post;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.dto.request.PostSaveDto;
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

    String url = "/api/posts";
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

        PostSaveDto postSaveDto = PostSaveDto.builder()
                .title("title1")
                .content("content1")
//                .boardType(Post.BoardType.FREE)
                .boardType(Post.BoardType.NEED_HELP)
                .tag(Post.Tag.TOGETHER)
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls)
                .isAnonymous(true)
                .build();


        String content = objectMapper.writeValueAsString(postSaveDto);
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(postSaveDto.getTitle()))
                .andExpect(jsonPath("content").value(postSaveDto.getContent()))
                .andExpect(jsonPath("anonymous").value(postSaveDto.getIsAnonymous()))
                .andExpect(jsonPath("writer.memberId").value(1))
                .andExpect(jsonPath("writer.nickname").value("익명"))
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



        PostSaveDto postSaveDto = PostSaveDto.builder()
                .title(titleLowerBound)
                .content("content1")
                .boardType(Post.BoardType.NEED_HELP)
                .tag(Post.Tag.TOGETHER)
                .majorCategory(Major.Category.ENGINEERING)
                .imageUrls(imageUrls)
                .isAnonymous(true)
                .build();



        String content = objectMapper.writeValueAsString(postSaveDto);
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    @DisplayName("실패: Enum 검증 실패")
    @WithMockMember
    public void failByInvalidRequestDataEnum() throws Exception {

        String img = objectMapper.writeValueAsString(imageUrls);

        Map<String, String> postSaveDto = new HashMap<>();
        postSaveDto.put("title","title1");
        postSaveDto.put("content","content1");
        postSaveDto.put("boardType","NEED_HELP");
//        postSaveDto.put("tag","WRONG");
        postSaveDto.put("imageUrls",img);
        postSaveDto.put("majorCategory","ENGINEERING");

        String content = objectMapper.writeValueAsString(postSaveDto);
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isCreated())
                .andDo(print());


    }
}
