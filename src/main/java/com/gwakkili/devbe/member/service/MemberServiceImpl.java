package com.gwakkili.devbe.member.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.image.entity.MemberImage;
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
                .major(memberSaveDto.getMajor())
                .school(memberSaveDto.getSchool())
                .build();
        member.setImage(MemberImage.builder().url(memberSaveDto.getImageUrl()).build());
        member.addRole(Member.Role.ROLE_USER);

        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailDto find(long memberId) {
        Member member = memberRepository.findWithCountById(memberId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        return MemberDetailDto.of(member);
    }

    @Override
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        Member member = memberRepository.findById(updatePasswordDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(updatePasswordDto.getPassword(), member.getPassword()))
            throw new BadCredentialsException("현재 비밀번호와 일치하지 않습니다.");

        member.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }

    @Override
    public void updateNicknameAndImage(UpdateNicknameAndImageDto updateNicknameAndImageDto) {
        Member member = memberRepository.findById(updateNicknameAndImageDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.setNickname(updateNicknameAndImageDto.getNickname());
        member.setImage(MemberImage.builder().url(updateNicknameAndImageDto.getImageUrl()).build());
    }

    @Override
    public void updateSchool(UpdateSchoolDto updateSchoolDto) {
        Member member = memberRepository.findById(updateSchoolDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.setMail(updateSchoolDto.getNewMail());
        member.setSchool(updateSchoolDto.getSchool());
    }

    @Override
    public void updateMajor(UpdateMajorDto updateMajorDto) {
        Member member = memberRepository.findById(updateMajorDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.setMajor(updateMajorDto.getMajor());
    }

    @Override
    public void lock(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.setLocked(true);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<MemberDto, Member> getList(SliceRequestDto sliceRequestDto) {
        MemberSliceRequestDto memberSliceRequestDto = (MemberSliceRequestDto) sliceRequestDto;
        String keyword = memberSliceRequestDto.getKeyword();
        Pageable pageable = memberSliceRequestDto.getPageable();
        Slice<Member> slice;

        if (StringUtils.hasText(memberSliceRequestDto.getKeyword())) {
            slice = memberRepository.findAllByMailContaining(keyword, pageable);
        } else slice = memberRepository.findAll(pageable);

        Function<Member, MemberDto> fn = (MemberDto::of);
        return new SliceResponseDto<>(slice, fn);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean mailDuplicateCheck(String mail) {
        return memberRepository.existsByMail(mail);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nicknameDuplicateCheck(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean passwordConfirm(String mail, String password) {
        Member member = memberRepository.findByMail(mail)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        return passwordEncoder.matches(password, member.getPassword());
    }
}
