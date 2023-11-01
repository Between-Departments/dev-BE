package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.security.dto.JwtTokenDto;
import org.springframework.security.core.Authentication;

public interface JwtService {

    JwtTokenDto generateToken(Authentication authentication);

    void saveRefreshToken(String refreshToken, Authentication authentication);

    void deleteRefreshToken(long memberId);

    Authentication getAuthenticationByAccessToken(String accessToken);

    Authentication getAuthenticationByRefreshToken(String refreshToken);
}
