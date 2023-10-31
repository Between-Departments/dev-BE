package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.LoginDto;
import com.gwakkili.devbe.security.dto.MemberDetails;

public interface AuthenticationService {

    JwtTokenDto login(LoginDto loginDto);

    void Logout(MemberDetails memberDetails);

    JwtTokenDto refresh(JwtTokenDto jwtTokenDto);
}
