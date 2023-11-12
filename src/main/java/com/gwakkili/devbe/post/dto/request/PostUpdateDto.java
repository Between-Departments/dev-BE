package com.gwakkili.devbe.post.dto.request;

import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Builder
public class PostUpdateDto {

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

    // TODO BoardType에 따라 majorCategory, tag 중 하나는 Null, 하나는 NotNull로 처리하는 로직 적용 고민
    @Schema(description = "도움이 필요해요 게시물용 전공 계열")
    private Major.Category majorCategory;

    @Schema(description = "자유게시판 게시물용 태그")
    private Post.Tag tag;

    @Schema(description = "게시물 익명 여부")
    @NotNull
    private Boolean isAnonymous;
}
