package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.validation.Major;
import lombok.Getter;

@Getter
public class PostSaveDto {

    private String title;

    private String content;

    @Major
    private String major;

    private Post.Category category;

    private boolean isAnonymous;
}
