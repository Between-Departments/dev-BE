package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.validation.Major;
import lombok.Getter;

import java.util.List;

@Getter
public class PostSaveDto {

    private String title;

    private String content;

    private List<String> imageUrls;

    @Major
    private String major;

    private Post.BoardType boardType;

    private Post.Tag tag;

    private boolean isAnonymous;
}
