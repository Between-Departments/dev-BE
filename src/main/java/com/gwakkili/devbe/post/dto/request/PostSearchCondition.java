package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchCondition {

    @Schema(description = "검색 키워드")
    private String keyword;

    @Schema(description = "게시판 종류")
    private Post.BoardType boardType;

    @Schema(description = "도움이 필요해요 게시물용 전공 계열")
    private Major.Category majorCategory;

    @Schema(description = "자유게시판 게시물용 태그")
    private Post.Tag tag;
}
