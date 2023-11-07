package com.gwakkili.devbe.post;

import com.gwakkili.devbe.DevBeApplicationTests;
import com.gwakkili.devbe.util.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("게시물 수정 테스트")
@Transactional
public class PostUpdateTests extends DevBeApplicationTests {

    String url = "/api/posts";

    @Test
    @DisplayName("성공")
    @WithMockMember
    public void success(){

        Long postId = 1L;

//        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
//                .title()
//                .content()
//                .imageUrls()
//                .majorCategory()
//                .tag()
//                .isAnonymous()
//                .build();
//
//        mockMvc.perform(patch(url, postId))
    }
}
