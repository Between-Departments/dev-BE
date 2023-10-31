package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.post.entity.Post;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class FreePostSaveDto extends PostSaveDto{

    @NotNull
    private Post.Tag tag;

}
