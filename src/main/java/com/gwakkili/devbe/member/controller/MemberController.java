package com.gwakkili.devbe.member.controller;

import com.gwakkili.devbe.member.dto.MemberDto;
import com.gwakkili.devbe.member.dto.MemberSaveDto;
import com.gwakkili.devbe.member.dto.NicknameAndImageDto;
import com.gwakkili.devbe.member.dto.UpdatePasswordDto;
import com.gwakkili.devbe.member.service.MemberService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Validated MemberSaveDto memberSaveDto) throws BindException {
        memberService.save(memberSaveDto);
    }

    // 나의 회원정보 조회
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public MemberDto find(@AuthenticationPrincipal MemberDetails memberDetails){
        return memberService.find(memberDetails.getMail());
    }

    // 비밀번호 변경
    @PatchMapping("/my/password")
    @PreAuthorize("isAuthenticated()")
    public void updatePassword(@AuthenticationPrincipal MemberDetails memberDetails,
                               @RequestBody @Validated UpdatePasswordDto updatePasswordDto,
                               BindingResult bindingResult) throws BindException {
        try {
            updatePasswordDto.setMail(memberDetails.getMail());
            memberService.updatePassword(updatePasswordDto);
        }catch (BadCredentialsException e){
            bindingResult.rejectValue("oldPassword", "", e.getMessage());
            throw new BindException(bindingResult);
        }
    }
    // 이미지, 닉네임 변경
    @PatchMapping("/my/image")
    @PreAuthorize("isAuthenticated()")
    public NicknameAndImageDto updateNicknameAndImage(@AuthenticationPrincipal MemberDetails memberDetails,
                                                      @RequestBody @Validated NicknameAndImageDto nicknameAndImageDto){
        nicknameAndImageDto.setMail(memberDetails.getMail());
        memberService.updateNicknameAndImage(nicknameAndImageDto);
        return nicknameAndImageDto;
    }

    //회원 정지
    @PatchMapping("/{id}/lock")
    public void lockMember(@PathVariable Long id){
        memberService.lock(id);
    }
}
