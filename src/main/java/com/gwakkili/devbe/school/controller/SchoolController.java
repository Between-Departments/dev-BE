package com.gwakkili.devbe.school.controller;

import com.gwakkili.devbe.dto.ListResponseDto;
import com.gwakkili.devbe.school.dto.SchoolDto;
import com.gwakkili.devbe.school.service.SchoolService;
import com.gwakkili.devbe.validation.annotation.School;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schools")
@Tag(name = "School", description = "학교 API")
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    @Operation(summary = "대학교 목록 조회")
    public ListResponseDto<SchoolDto, School> getSchoolList(@Parameter(name = "keyword", description = "검색 키워드") String keyword) {
        return schoolService.getSchoolList(keyword);
    }
}
