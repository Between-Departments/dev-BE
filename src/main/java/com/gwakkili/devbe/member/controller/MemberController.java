package com.gwakkili.devbe.member.controller;

import com.gwakkili.devbe.member.dto.MemberDto;
import com.gwakkili.devbe.member.dto.MemberSaveDto;
import com.gwakkili.devbe.member.service.MemberService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public MemberDto find(@AuthenticationPrincipal MemberDetails memberDetails){
        return memberService.find(memberDetails.getMail());
    }
}
