package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
public class PostUpdateDto {

    @Length(min = 3, max = 30)
    private String title;

    @Length(min = 5, max = 1000)
    private String content;

    private List<String> imageUrls;

    private Major.Category majorCategory;

    private Post.BoardType boardType;

    private Post.Tag tag;

    @NotNull
    private boolean isAnonymous;
}
