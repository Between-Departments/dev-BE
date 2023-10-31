package com.gwakkili.devbe.reply.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema
public class ReplyUpdateDto {

    @JsonIgnore
    private long replyId;

    @JsonIgnore
    private long memberId;

    @Schema(description = "게시글 내용", example = "안녕하세요~")
    @NotBlank
    @Length(max = 500)
    private String content;

    public ReplyUpdateDto(String content) {
        this.content = content;
    }
}
