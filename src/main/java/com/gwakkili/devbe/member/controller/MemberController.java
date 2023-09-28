package com.gwakkili.devbe.member.controller;

import com.gwakkili.devbe.member.dto.MemberSaveDto;
import com.gwakkili.devbe.member.service.MemberService;
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
    public void save(@RequestBody @Validated MemberSaveDto memberSaveDto) throws BindException {
        memberService.save(memberSaveDto);
    }
}
