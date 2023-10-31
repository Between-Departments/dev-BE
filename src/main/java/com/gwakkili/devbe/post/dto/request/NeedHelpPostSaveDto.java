package com.gwakkili.devbe.post.dto.request;


import com.gwakkili.devbe.major.entity.Major;
import com.gwakkili.devbe.validation.annotation.Enum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class NeedHelpPostSaveDto extends PostSaveDto{

    @Enum(message = "Invalid MajorCategory!")
    private Major.Category majorCategory;

}
