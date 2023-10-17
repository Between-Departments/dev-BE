package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.validation.Major;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class PostSaveDto {

    @Size(min = 3, max = 30)
    private String title;

    @Size(min = 10, max = 1000)
    private String content;

    private List<String> imageUrls;

    @Major
    private String major;

    private Post.BoardType boardType;

    private Post.Tag tag;

    private boolean isAnonymous;
}
