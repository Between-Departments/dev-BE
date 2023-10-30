package com.gwakkili.devbe.school.dto;

import com.gwakkili.devbe.school.entity.School;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
@Schema
public class SchoolDto {

    @Schema(description = "학교 번호", example = "1")
    long schoolId;

    @Schema(description = "학교 메일", example = "sun.ac.kr")
    String mail;

    @Schema(description = "학교 이름", example = "서울대학교")
    String name;

    public static SchoolDto of(School school) {
        return SchoolDto.builder()
                .schoolId(school.getSchoolId())
                .mail(school.getMail())
                .name(school.getName())
                .build();
    }
}
