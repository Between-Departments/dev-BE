package com.gwakkili.devbe.member.controller;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.member.dto.*;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.service.MemberService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.validation.Major;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원 가입")
    public void save(@RequestBody @Validated MemberSaveDto memberSaveDto) throws BindException {
        memberService.save(memberSaveDto);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "나의 회원정보 조회")
    public MemberDto find(@AuthenticationPrincipal MemberDetails memberDetails){
        return memberService.find(memberDetails.getMail());
    }


    @GetMapping("/mail/duplicate")
    @Operation(summary = "메일 중복 획안")
    public boolean mailDuplicateCheck(@Parameter(name = "mail", description = "메일") String mail) {
        return memberService.mailDuplicateCheck(mail);
    }

    @GetMapping("/nickname/duplicate")
    @Operation(summary = "닉네임 중복 확인")
    public boolean nicknameDuplicateCheck(@Parameter(name = "nickname", description = "닉네임") String nickname) {
        return memberService.nicknameDuplicateCheck(nickname);
    }

    @PostMapping("/password/confirm")
    @Operation(summary = "비밀번호 확인")
    @PreAuthorize("isAuthenticated()")
    public boolean passwordConfirm(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody String password) {
        return memberService.passwordConfirm(memberDetails.getMail(), password);
    }

    @PatchMapping("/my/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "비밀번호 변경")
    public void updatePassword(@AuthenticationPrincipal MemberDetails memberDetails,
                               @RequestBody @Validated UpdatePasswordDto updatePasswordDto,
                               BindingResult bindingResult) throws BindException {
        try {
            updatePasswordDto.setMail(memberDetails.getMail());
            memberService.updatePassword(updatePasswordDto);
        } catch (BadCredentialsException e) {
            bindingResult.rejectValue("oldPassword", "", e.getMessage());
            throw new BindException(bindingResult);
        }
    }

    @PatchMapping("/my/image")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "이미지, 닉네임 변경")
    public NicknameAndImageDto updateNicknameAndImage(@AuthenticationPrincipal MemberDetails memberDetails,
                                                      @RequestBody @Validated NicknameAndImageDto nicknameAndImageDto) {
        nicknameAndImageDto.setMail(memberDetails.getMail());
        memberService.updateNicknameAndImage(nicknameAndImageDto);
        return nicknameAndImageDto;
    }

    @PatchMapping("/my/school")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "학교 정보 변경")
    public void updateSchool(@AuthenticationPrincipal MemberDetails memberDetails,
                             @RequestBody @Validated UpdateSchoolDto updateSchoolDto) {
        updateSchoolDto.setOldMail(memberDetails.getMail());
        memberService.updateSchool(updateSchoolDto);
    }

    @PatchMapping("/my/major")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "학과 정보 변경")
    public void updateMajor(@AuthenticationPrincipal MemberDetails memberDetails,
                            @RequestBody @Validated UpdateMajorDto updateMajorDto) {
        updateMajorDto.setMail(memberDetails.getMail());
        memberService.updateMajor(updateMajorDto);
    }

    @PatchMapping("/{id}/lock")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "회원 정지")
    public void lockMember(@Parameter(name = "id", description = "회원 번호", in = ParameterIn.PATH) @PathVariable Long id) {
        memberService.lock(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "회원 목록 조회")
    public SliceResponseDto<MemberDto, Member> getList(@ParameterObject SliceRequestDto sliceRequestDto) {
        return memberService.getList(sliceRequestDto);
    }

}
