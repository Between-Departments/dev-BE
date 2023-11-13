package com.gwakkili.devbe.post.dto.request;


import com.gwakkili.devbe.major.entity.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class NeedHelpPostSaveDto extends PostSaveDto{

    @Schema(description = "도움이 필요해요 게시물용 전공 계열")
    @NotNull
    private Major.Category majorCategory;

}
