package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.validation.Enum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSearchCondition {

    private String keyword;

    @Enum(message = "Invalid BoardType!")
    private Post.BoardType boardType;

    @Enum(message = "Invalid MajorCategory!")
    private Major.Category majorCategory;

    @Enum(message = "Invalid Tag!")
    private Post.Tag tag;

    public PostSearchCondition(Post.BoardType boardType) {
        this.boardType = boardType;
    }

    public PostSearchCondition(String keyword, Post.BoardType boardType, Major.Category majorCategory, Post.Tag tag) {
        this.keyword = keyword;
        this.boardType = boardType;
        this.majorCategory = majorCategory;
        this.tag = tag;
    }

}
