package com.gwakkili.devbe.member.service;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.member.dto.MemberDto;
import com.gwakkili.devbe.member.dto.MemberSaveDto;
import com.gwakkili.devbe.member.dto.UpdatePasswordDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public void updatePassword(String mail, UpdatePasswordDto updatePasswordDto) {
        Member member = memberRepository.findByMail(mail)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        if(!passwordEncoder.matches(updatePasswordDto.getOldPassword(), member.getPassword()))
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

        member.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }
}
