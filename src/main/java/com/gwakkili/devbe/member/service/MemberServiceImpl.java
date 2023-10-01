package com.gwakkili.devbe.member.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.dto.*;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void save(MemberSaveDto memberSaveDto) {

        Member member = Member.builder()
                .mail(memberSaveDto.getMail())
                .nickname(memberSaveDto.getNickname())
                .password(passwordEncoder.encode(memberSaveDto.getPassword()))
                .school(memberSaveDto.getSchool())
                .major(memberSaveDto.getMajor())
                .imageUrl(memberSaveDto.getImageUrl())
                .build();
        member.addRole(Member.Role.ROLE_USER);

        memberRepository.save(member);

    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto find(String mail) {
        Member member = memberRepository.findByMail(mail)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        return MemberDto.of(member);
    }

    @Override
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        Member member = memberRepository.findByMail(updatePasswordDto.getMail())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        if(!passwordEncoder.matches(updatePasswordDto.getOldPassword(), member.getPassword()))
            throw new BadCredentialsException("현재 비밀번호와 일치하지 않습니다.");

        member.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }

    @Override
    public void updateNicknameAndImage(NicknameAndImageDto nicknameAndImageDto) {
        Member member = memberRepository.findByMail(nicknameAndImageDto.getMail())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.setNickname(nicknameAndImageDto.getNickname());
        member.setImageUrl(nicknameAndImageDto.getImageUrl());
    }

    @Override
    public void lock(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.setLocked(true);
    }

    @Override
    public SliceResponseDto<MemberDto, Member> getList(SliceRequestDto sliceRequestDto) {

        String keyword = sliceRequestDto.getKeyword();
        Pageable pageable = sliceRequestDto.getPageable();
        Slice<Member> slice;

        System.out.println(pageable);
        if(StringUtils.hasText(sliceRequestDto.getKeyword())) {
            slice = memberRepository.findAllByMailContaining(keyword, pageable);
        } else slice =memberRepository.findAll(pageable);

        Function<Member, MemberDto> fn = (MemberDto::of);
        return new SliceResponseDto<>(slice, fn);
    }
}
