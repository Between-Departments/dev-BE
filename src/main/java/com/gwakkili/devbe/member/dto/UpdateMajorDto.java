package com.gwakkili.devbe.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.Major;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UpdateMajorDto {

    @JsonIgnore
    String mail;

    @NotBlank
    @Major
    String major;
}
