package com.gwakkili.devbe.post.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class PostSaveDto {

    @Length(min = 3, max = 30)
    private String title;

    @Length(min = 5, max = 1000)
    private String content;

    // ? 프로젝트에서 사용하고 있는 S3 서버의 url이 맞는지에 대한 검증이 필요한가?
    private List<String> imageUrls;

    @NotNull
    private Boolean isAnonymous;
  
}
