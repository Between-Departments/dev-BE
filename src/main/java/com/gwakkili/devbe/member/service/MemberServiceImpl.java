package com.gwakkili.devbe.member.service;

import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.event.DeleteByManagerEvent;
import com.gwakkili.devbe.event.DeleteMemberImageEvent;
import com.gwakkili.devbe.event.DeletePostImageEvent;
import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.CustomException;
import com.gwakkili.devbe.exception.customExcption.NotFoundException;
import com.gwakkili.devbe.image.entity.MemberImage;
import com.gwakkili.devbe.image.entity.PostImage;
import com.gwakkili.devbe.image.repository.MemberImageRepository;
import com.gwakkili.devbe.image.repository.PostImageRepository;
import com.gwakkili.devbe.member.dto.request.*;
import com.gwakkili.devbe.member.dto.response.MemberDetailDto;
import com.gwakkili.devbe.member.dto.response.MemberDto;
import com.gwakkili.devbe.member.entity.Member;
import com.gwakkili.devbe.member.repository.MemberRepository;
import com.gwakkili.devbe.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final MemberImageRepository memberImageRepository;

    private final PostImageRepository postImageRepository;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    public void saveMember(MemberSaveDto memberSaveDto) {

        Member member = Member.builder()
                .mail(memberSaveDto.getMail())
                .nickname(memberSaveDto.getNickname())
                .password(passwordEncoder.encode(memberSaveDto.getPassword()))
                .major(memberSaveDto.getMajor())
                .school(memberSaveDto.getSchool())
                .build();
        member.setImage(new MemberImage(memberSaveDto.getImageUrl()));
        member.addRole(Member.Role.ROLE_USER);

        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailDto findMember(long memberId) {
        Member member = memberRepository.findWithCountById(memberId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        return MemberDetailDto.of(member);
    }

    @Override
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        Member member = memberRepository.findById(updatePasswordDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));

        if (!passwordEncoder.matches(updatePasswordDto.getPassword(), member.getPassword()))
            throw new CustomException(ExceptionCode.INVALID_PASSWORD);

        member.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
    }

    @Override
    public void updateNicknameAndImage(UpdateNicknameAndImageDto updateNicknameAndImageDto) {
        Member member = memberRepository.findById(updateNicknameAndImageDto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        memberImageRepository.deleteByMember(member);
        member.setNickname(updateNicknameAndImageDto.getNickname());
        member.setImage(new MemberImage(updateNicknameAndImageDto.getImageUrl()));
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
    public void deleteMember(long memberId, String password) {
        Member member = memberRepository.findWithImageAndPostsByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new CustomException(ExceptionCode.INVALID_PASSWORD);

        eventPublisher.publishEvent(new DeleteMemberImageEvent(member.getImage().getUrl()));

        List<Post> posts = member.getPosts();
        if (!posts.isEmpty()) {
            List<PostImage> postImages = postImageRepository.findByPostIn(posts);
            if (!postImages.isEmpty()) {
                List<String> imageUrls = postImages.stream().map(PostImage::getUrl).collect(Collectors.toList());
                eventPublisher.publishEvent(new DeletePostImageEvent(imageUrls));
            }
        }

        memberRepository.delete(member);
    }

    @Override
    public void lockMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.setLocked(true);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void lock(DeleteByManagerEvent deleteByManagerEvent) {
        long memberId = deleteByManagerEvent.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_MEMBER));
        member.addReportCount();
        if (member.getReportCount() >= 3) member.setLocked(true);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponseDto<MemberDto, Member> getMemberList(SliceRequestDto sliceRequestDto) {
        MemberSliceRequestDto memberSliceRequestDto = (MemberSliceRequestDto) sliceRequestDto;
        String keyword = memberSliceRequestDto.getKeyword();
        Pageable pageable = memberSliceRequestDto.getPageable();
        Slice<Member> slice;

        if (StringUtils.hasText(memberSliceRequestDto.getKeyword())) {
            slice = memberRepository.findAllByMailContaining(keyword, pageable);
        } else slice = memberRepository.findAllWithImage(pageable);

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
