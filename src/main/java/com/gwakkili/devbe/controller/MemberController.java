package com.gwakkili.devbe.controller;

import com.gwakkili.devbe.dto.MemberDto;
import com.gwakkili.devbe.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {



    private final MemberService memberService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Validated MemberDto.Save saveDto) throws BindException {
        memberService.save(saveDto);
    }
}
