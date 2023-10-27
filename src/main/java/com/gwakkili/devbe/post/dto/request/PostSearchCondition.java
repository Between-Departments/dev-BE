package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.validation.Enum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchCondition {

    private String keyword;

    @Enum(message = "Invalid BoardType!")
    private Post.BoardType boardType;

    @Enum(message = "Invalid MajorCategory!")
    private Major.Category majorCategory;

    @Enum(message = "Invalid Tag!")
    private Post.Tag tag;


}
