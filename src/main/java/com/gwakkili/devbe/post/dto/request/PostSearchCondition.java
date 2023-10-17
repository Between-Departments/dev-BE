package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.validation.Major;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSearchCondition {

    private String keyword;

    private Post.BoardType boardType;

    @Major
    private String major;

    private Post.Tag tag;

    public PostSearchCondition(String keyword, Post.BoardType boardType, String major, Post.Tag tag) {
        this.keyword = keyword;
        this.boardType = boardType;
        this.major = major;
        this.tag = tag;
    }
}
