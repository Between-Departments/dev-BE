package com.gwakkili.devbe.school.controller;

import com.gwakkili.devbe.school.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schools")
@Tag(name = "School", description = "학교 API")
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    @Operation(summary = "대학교 이름목록 조회")
    List<String> getNameList(@Parameter(name = "keyword", description = "검색 키워드") String keyword) {
        return schoolService.getNameList(keyword);
    }
}