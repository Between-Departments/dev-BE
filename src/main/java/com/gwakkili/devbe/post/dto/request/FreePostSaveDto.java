package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.validation.Enum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class FreePostSaveDto extends PostSaveDto{

    @Enum(message = "Invalid Tag!")
    private Post.Tag tag;

}
