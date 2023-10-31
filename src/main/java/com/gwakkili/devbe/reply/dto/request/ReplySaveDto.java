package com.gwakkili.devbe.reply.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema
public class ReplySaveDto {

    @JsonIgnore
    private long writer;

    @Schema(description = "게시글 번호", example = "3")
    private long postId;

    @Schema(description = "게시글 내용", example = "안녕하세요~")
    @Length(max = 500)
    @NotBlank
    private String content;

    @Schema(description = "게시글 익명 여부")
    private boolean isAnonymous;

    @Builder
    public ReplySaveDto(long postId, String content, boolean isAnonymous) {
        this.postId = postId;
        this.content = content;
        this.isAnonymous = isAnonymous;
    }
}
