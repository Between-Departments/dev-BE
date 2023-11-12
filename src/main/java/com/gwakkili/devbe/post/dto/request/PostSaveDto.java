package com.gwakkili.devbe.post.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "게시물 제목")
    @Length(min = 2, max = 30)
    private String title;

    @Schema(description = "게시물 본문")
    @Length(min = 2, max = 1000)
    private String content;

    @Schema(description = "게시물 첨부 이미지 링크")
    @Size(max = 3)
    @Valid
    private List<@URL String> imageUrls;

    @Schema(description = "게시물 익명 여부")
    @NotNull
    private Boolean isAnonymous;
  
}
