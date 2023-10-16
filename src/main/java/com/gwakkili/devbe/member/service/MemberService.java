package com.gwakkili.devbe.member.service;


import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.event.DeleteByManagerEvent;
import com.gwakkili.devbe.member.dto.request.*;
import com.gwakkili.devbe.member.dto.response.MemberDetailDto;

public interface MemberService {

    void save(MemberSaveDto saveDto);

    MemberDetailDto find(long memberId);

    void updatePassword(UpdatePasswordDto updatePasswordDto);

    void updateNicknameAndImage(UpdateNicknameAndImageDto updateNicknameAndImageDto);

    void updateSchool(UpdateSchoolDto updateSchoolDto);

    void updateMajor(UpdateMajorDto updateMajorDto);

    void lock(Long id);

    void lock(DeleteByManagerEvent deleteByManagerEvent);

    SliceResponseDto getList(SliceRequestDto sliceRequestDto);

    boolean mailDuplicateCheck(String mail);

    boolean nicknameDuplicateCheck(String nickname);

    boolean passwordConfirm(String mail, String password);

}
