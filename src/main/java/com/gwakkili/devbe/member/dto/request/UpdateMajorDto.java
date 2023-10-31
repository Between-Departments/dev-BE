package com.gwakkili.devbe.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gwakkili.devbe.validation.annotation.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMajorDto {

    @JsonIgnore
    long memberId;

    @NotBlank
    @Major
    @Schema(description = "학과", example = "켐퓨터공학과")
    String major;

    public UpdateMajorDto(String major) {
        this.major = major;
    }
}
