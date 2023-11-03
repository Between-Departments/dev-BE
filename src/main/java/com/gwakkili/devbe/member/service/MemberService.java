package com.gwakkili.devbe.member.service;


import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.member.dto.request.*;
import com.gwakkili.devbe.member.dto.response.MemberDetailDto;

public interface MemberService {

    void saveMember(MemberSaveDto saveDto);

    MemberDetailDto findMember(long memberId);

    void updatePassword(UpdatePasswordDto updatePasswordDto);

    void updateNicknameAndImage(UpdateNicknameAndImageDto updateNicknameAndImageDto);

    void updateSchool(UpdateSchoolDto updateSchoolDto);

    void updateMajor(UpdateMajorDto updateMajorDto);

    void deleteMember(long memberId, String password);

    void lockMember(Long id);

    SliceResponseDto getMemberList(SliceRequestDto sliceRequestDto);

    boolean mailDuplicateCheck(String mail);

    boolean nicknameDuplicateCheck(String nickname);

    boolean passwordConfirm(String mail, String password);

}
