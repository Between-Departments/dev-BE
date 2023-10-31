package com.gwakkili.devbe.member.controller;

import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.member.dto.request.MemberSliceRequestDto;
import com.gwakkili.devbe.member.dto.response.MemberDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "AdminMember", description = "어드민 전용 회원 API")
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequestMapping("/api/members")
public class AdminMemberController {

    private final MemberService memberService;

    @PatchMapping("/{memberId}/lock")
    @Operation(summary = "회원 정지")
    public void lockMember(@Parameter(name = "memberId", description = "회원 번호", in = ParameterIn.PATH) @PathVariable Long memberId) {
        memberService.lockMember(memberId);
    }

    @GetMapping
    @Operation(summary = "회원 목록 조회")
    public SliceResponseDto<MemberDto, Member> getMemberList(@ParameterObject MemberSliceRequestDto sliceRequestDto) {
        return memberService.getMemberList(sliceRequestDto);
    }

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "회원 탈퇴")
    public void deleteMember(@Parameter(name = "memberId", description = "회원 번호", in = ParameterIn.PATH) @PathVariable Long memberId) {
        memberService.deleteMember(memberId);
    }
}
