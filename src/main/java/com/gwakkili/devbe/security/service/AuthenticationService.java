package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.LoginDto;

public interface AuthenticationService {

    JwtTokenDto login(LoginDto loginDto);

    void Logout(long memberId);

    JwtTokenDto refresh(JwtTokenDto jwtTokenDto);
}
