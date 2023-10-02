package com.gwakkili.devbe.shcool.controller;

import com.gwakkili.devbe.shcool.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schools")
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    List<String> getNameList(String keyword) {
        return schoolService.getNameList(keyword);
    }
}
