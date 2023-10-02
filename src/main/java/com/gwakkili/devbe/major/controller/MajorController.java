package com.gwakkili.devbe.major.controller;

import com.gwakkili.devbe.major.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/majors")
public class MajorController {

    private final MajorService majorService;

    @GetMapping
    List<String> getNameList(String keyword) {
        return majorService.getNameList(keyword);
    }
}
