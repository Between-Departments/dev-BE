package com.gwakkili.devbe.member.service;


import com.gwakkili.devbe.member.dto.MemberDto;
import com.gwakkili.devbe.member.dto.MemberSaveDto;

public interface MemberService {

    void save(MemberSaveDto saveDto);

    MemberDto find(String mail);

}
