package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostSaveDto {

    private String title;

    private String content;

    private List<String> imageUrls;

    private Major.Category majorCategory;

    private Post.BoardType boardType;

    private Post.Tag tag;

    private Boolean isAnonymous;
}
