package com.gwakkili.devbe.member.service;


import com.gwakkili.devbe.member.dto.MemberDto;
import com.gwakkili.devbe.member.dto.MemberSaveDto;
import com.gwakkili.devbe.member.dto.UpdatePasswordDto;

public interface MemberService {

    void save(MemberSaveDto saveDto);

    MemberDto find(String mail);

    void updatePassword(String mail, UpdatePasswordDto updatePasswordDto);

}
