package com.gwakkili.devbe.member.controller;

import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.member.dto.request.*;
import com.gwakkili.devbe.member.dto.response.MemberDetailDto;
import com.gwakkili.devbe.member.dto.response.MemberDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.service.MemberService;
import com.gwakkili.devbe.security.dto.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public void save(@RequestBody @Validated MemberSaveDto memberSaveDto) {
        memberService.save(memberSaveDto);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "나의 회원정보 조회")
    public MemberDetailDto find(@AuthenticationPrincipal MemberDetails memberDetails) {
        return memberService.find(memberDetails.getMemberId());
    }


    @GetMapping("/mail/duplicate")
    @Operation(summary = "메일 중복 획안")
    public void mailDuplicateCheck(@Parameter(name = "mail", description = "메일") String mail) {
        if (memberService.mailDuplicateCheck(mail))
            throw new CustomException(ExceptionCode.DUPLICATE_MAIL);

    }

    @GetMapping("/nickname/duplicate")
    @Operation(summary = "닉네임 중복 확인")
    public void nicknameDuplicateCheck(@Parameter(name = "nickname", description = "닉네임") String nickname) {
        if (memberService.nicknameDuplicateCheck(nickname))
            throw new CustomException(ExceptionCode.DUPLICATE_NICKNAME);

    }

    @PostMapping("/password/confirm")
    @Operation(summary = "비밀번호 확인")
    @PreAuthorize("isAuthenticated()")
    public void passwordConfirm(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody String password) {
        if (memberService.passwordConfirm(memberDetails.getMail(), password))
            throw new CustomException(ExceptionCode.INVALID_PASSWORD);
    }

    @PatchMapping("/my/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "비밀번호 변경")
    public void updatePassword(@AuthenticationPrincipal MemberDetails memberDetails,
                               @RequestBody @Validated UpdatePasswordDto updatePasswordDto,
                               BindingResult bindingResult) throws BindException {
        try {
            updatePasswordDto.setMemberId(memberDetails.getMemberId());
            memberService.updatePassword(updatePasswordDto);
        } catch (BadCredentialsException e) {
            bindingResult.rejectValue("password", "", e.getMessage());
            throw new BindException(bindingResult);
        }
    }

    @PatchMapping("/my/image")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "이미지, 닉네임 변경")
    public UpdateNicknameAndImageDto updateNicknameAndImage(@AuthenticationPrincipal MemberDetails memberDetails,
                                                            @RequestBody @Validated UpdateNicknameAndImageDto updateNicknameAndImageDto) {
        updateNicknameAndImageDto.setMemberId(memberDetails.getMemberId());
        memberService.updateNicknameAndImage(updateNicknameAndImageDto);
        return updateNicknameAndImageDto;
    }

    @PatchMapping("/my/school")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "학교 정보 변경")
    public void updateSchool(@AuthenticationPrincipal MemberDetails memberDetails,
                             @RequestBody @Validated UpdateSchoolDto updateSchoolDto) {
        updateSchoolDto.setMemberId(memberDetails.getMemberId());
        memberService.updateSchool(updateSchoolDto);
    }

    @PatchMapping("/my/major")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "학과 정보 변경")
    public void updateMajor(@AuthenticationPrincipal MemberDetails memberDetails,
                            @RequestBody @Validated UpdateMajorDto updateMajorDto) {
        updateMajorDto.setMemberId(memberDetails.getMemberId());
        memberService.updateMajor(updateMajorDto);
    }

    @PatchMapping("/{memberId}/lock")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "회원 정지")
    public void lockMember(@Parameter(name = "memberId", description = "회원 번호", in = ParameterIn.PATH) @PathVariable Long memberId) {
        memberService.lock(memberId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "회원 목록 조회")
    public SliceResponseDto<MemberDto, Member> getList(@ParameterObject MemberSliceRequestDto sliceRequestDto) {
        return memberService.getList(sliceRequestDto);
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "회원 탈퇴")
    public void delete(@Parameter(name = "memberId", description = "회원 번호", in = ParameterIn.PATH) @PathVariable Long memberId,
                       @AuthenticationPrincipal MemberDetails memberDetails) {

    }
}
