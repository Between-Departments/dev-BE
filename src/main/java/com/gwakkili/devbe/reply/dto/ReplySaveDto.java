package com.gwakkili.devbe.reply.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.post.entity.Post;
import com.gwakkili.devbe.reply.entity.Reply;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema
public class ReplySaveDto {

    @JsonIgnore
    private long writer;

    @Schema(description = "게시글 번호", example = "3")
    private long postId;

    @Schema(description = "게시글 내용", example = "안녕하세요~")
    @Length(max = 100)
    @NotBlank
    private String content;

    @Schema(description = "익명 여부")
    private boolean isAnonymous;

}
