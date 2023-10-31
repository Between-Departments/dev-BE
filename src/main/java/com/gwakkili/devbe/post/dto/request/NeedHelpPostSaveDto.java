package com.gwakkili.devbe.post.dto.request;


import com.gwakkili.devbe.major.entity.Major;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class NeedHelpPostSaveDto extends PostSaveDto{

    @NotNull
    private Major.Category majorCategory;

}
