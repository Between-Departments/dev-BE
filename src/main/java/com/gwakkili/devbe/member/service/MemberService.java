package com.gwakkili.devbe.member.service;


import com.gwakkili.devbe.dto.SliceRequestDto;
import com.gwakkili.devbe.dto.SliceResponseDto;
import com.gwakkili.devbe.member.dto.*;

public interface MemberService {

    void save(MemberSaveDto saveDto);

    MemberDto find(String mail);

    void updatePassword(UpdatePasswordDto updatePasswordDto);

    void updateNicknameAndImage(NicknameAndImageDto nicknameAndImageDto);

    void lock(Long id);

    SliceResponseDto  getList(SliceRequestDto sliceRequestDto);

}
