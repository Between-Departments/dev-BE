package com.gwakkili.devbe.major.dto;

import com.gwakkili.devbe.major.entity.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
@Schema
public class MajorDto {

    @Schema(description = "전공 번호", example = "1")
    long majorId;

    @Schema(description = "전공 계열")
    Major.Category category;

    @Schema(description = "전공 이름", example = "컴퓨터공학과")
    String name;

    public static MajorDto of(Major major) {
        return MajorDto.builder()
                .majorId(major.getMajorId())
                .category(major.getCategory())
                .name(major.getName())
                .build();
    }
}
