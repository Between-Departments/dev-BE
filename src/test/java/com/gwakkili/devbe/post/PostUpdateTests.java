package com.gwakkili.devbe.post;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.post.dto.request.PostUpdateDto;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시물 수정 테스트")
@Transactional
public class PostUpdateTests extends DevBeApplicationTests {

    String url = "/api/posts/{postId}";

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success() throws Exception {
        Long postId = 1L;

        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .title("수정된 제목입니다!")
                .content("수정된 본문입니다!")
                .tag(Post.Tag.TOGETHER)
                .isAnonymous(true)
                .build();

        String content = objectMapper.writeValueAsString(postUpdateDto);

        mockMvc.perform(patch(url, postId).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andDo(print());

        em.flush();
        em.clear();
        System.out.println("=============================================================================================================================================================================================");

        mockMvc.perform(get(url,postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("postId").value(postId))
                .andExpect(jsonPath("title").value(postUpdateDto.getTitle()))
                .andExpect(jsonPath("content").value(postUpdateDto.getContent()))
                .andExpect(jsonPath("boardType").value(Post.BoardType.FREE.name()))
                .andExpect(jsonPath("tag").value(postUpdateDto.getTag().name()))
                .andExpect(jsonPath("isAnonymous").value(postUpdateDto.getIsAnonymous()))
                .andExpect(jsonPath("writer.memberId").doesNotExist())
                .andExpect(jsonPath("writer.nickname").value("익명"))
                .andExpect(jsonPath("writer.school").doesNotExist())
                .andExpect(jsonPath("writer.major").doesNotExist())
                .andExpect(jsonPath("recommendCount").value(20))
                .andExpect(jsonPath("replyCount").value(20))
                .andExpect(jsonPath("images").doesNotExist())
                .andExpect(jsonPath("isAnonymous").value(Boolean.TRUE))
                .andExpect(jsonPath("isMine").value(Boolean.TRUE))
                .andExpect(jsonPath("isBookmarked").value(Boolean.TRUE))
                .andExpect(jsonPath("isRecommended").value(Boolean.TRUE))
                .andDo(print());
    }

    @DisplayName("실패: 권한 없음")
    @Test
    @WithMockMember(memberId = 5)
    public void failByAccessDenied() throws Exception {
        Long postId = 1L;

        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .title("수정 권한이 없습니다!")
                .content("본인이 작성한 글만 수정할수 있습니다.")
                .isAnonymous(Boolean.TRUE)
                .build();

        String content = objectMapper.writeValueAsString(postUpdateDto);

        mockMvc.perform(patch(url, postId).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value(ExceptionCode.ACCESS_DENIED.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.ACCESS_DENIED.getMessage()))
                .andDo(print());
    }

    @DisplayName("실패: 유효성 검증 실패")
    @Test
    @WithMockMember
    public void failByInvalid() throws Exception {
        Long postId = 1L;

        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("wrong_image1_url");
        imageUrls.add("wrong_image2_url");
        imageUrls.add("wrong_image3_url");
        imageUrls.add("wrong_image4_url");

        Map<String, Object> postUpdateDto = new HashMap<>();
        postUpdateDto.put("title","1");
        postUpdateDto.put("content","1");
        postUpdateDto.put("imageUrls", imageUrls);

        String content = objectMapper.writeValueAsString(postUpdateDto);

        //when, then
        mockMvc.perform(patch(url, postId).contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(ExceptionCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("message").value(ExceptionCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("fieldErrors",aMapWithSize(8)))
                .andExpect(jsonPath("fieldErrors.content").exists())
                .andExpect(jsonPath("fieldErrors.title").exists())
                .andExpect(jsonPath("fieldErrors.isAnonymous").exists())
                .andExpect(jsonPath("fieldErrors.imageUrls").exists())
                .andDo(print());
    }


}
