package com.gwakkili.devbe.post.dto.response;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public abstract class AbstractPostListDto {

    private long postId;

    private String title;

    private String content;

    private int viewCount;

    private int recommendCount;

    private int replyCount;

    private LocalDateTime createAt;
}
