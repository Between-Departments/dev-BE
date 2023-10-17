package com.gwakkili.devbe.post.dto.response;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
public abstract class AbstractPostListDto {

    private long postId;

    private String title;

    private String content;

    private List<String> thumbnailImages;

    private long viewCount;

    private long recommendCount;

    private long replyCount;

    private LocalDateTime createAt;
}
