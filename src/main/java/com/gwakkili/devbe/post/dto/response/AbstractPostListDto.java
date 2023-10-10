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

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
