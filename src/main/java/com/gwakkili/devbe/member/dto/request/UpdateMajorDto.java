package com.gwakkili.devbe.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Schema
public class UpdateMajorDto {

    @JsonIgnore
    long memberId;

    @NotBlank
    @Major
    @Schema(description = "학과", example = "켐퓨터공학과")
    String major;
}
