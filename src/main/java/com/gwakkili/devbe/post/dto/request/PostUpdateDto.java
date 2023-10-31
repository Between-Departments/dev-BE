package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
public class PostUpdateDto {

    @Length(min = 3, max = 30)
    private String title;

    @Length(min = 5, max = 1000)
    private String content;

    @Size(max = 3)
    @Valid
    private List<@URL String> imageUrls;

    // ! BoardType에 따라 majorCategory, tag 중 하나는 Null, 하나는 NotNull이어야 하는데 그걸 어떻게 처리하지?
    private Major.Category majorCategory;

    private Post.Tag tag;

    @NotNull
    private boolean isAnonymous;
}
