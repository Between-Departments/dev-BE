package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchCondition {

    private String keyword;

    private Post.BoardType boardType;

    private Major.Category majorCategory;

    private Post.Tag tag;
}
