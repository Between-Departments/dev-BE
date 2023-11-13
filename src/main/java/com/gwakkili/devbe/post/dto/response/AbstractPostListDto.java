package com.gwakkili.devbe.post.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class AbstractPostListDto {

    private Long postId;

    private String title;

    private String content;

    private Post.BoardType boardType;

    private Major.Category majorCategory;

    private Post.Tag tag;

    private List<String> thumbnailImages;

    private Long viewCount;

    private Long recommendCount;

    private Long replyCount;

    private LocalDateTime createAt;
}
