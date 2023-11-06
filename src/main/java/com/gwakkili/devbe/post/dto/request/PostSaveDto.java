package com.gwakkili.devbe.post.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class PostSaveDto {

    @Length(min = 2, max = 30)
    private String title;

    @Length(min = 10, max = 1000)
    private String content;

    @Size(max = 3)
    @Valid
    private List<@URL String> imageUrls;

    @NotNull
    private Boolean isAnonymous;
  
}
