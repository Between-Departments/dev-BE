package com.gwakkili.devbe.major.controller;

import com.gwakkili.devbe.major.service.MajorService;
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
@RequestMapping("/api/majors")
@Tag(name = "Major", description = "학과 API")
public class MajorController {

    private final MajorService majorService;

    @GetMapping
    @Operation(summary = "전공 이름목록 조회")
    List<String> getNameList(@Parameter(name = "keyword", description = "검색 키워드") String keyword) {
        return majorService.getNameList(keyword);
    }
}
