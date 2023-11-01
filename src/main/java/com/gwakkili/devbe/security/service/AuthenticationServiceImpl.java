package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.LoginDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public JwtTokenDto login(LoginDto loginDto) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getMail(), loginDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        JwtTokenDto jwtTokenDto = jwtService.generateToken(authentication);
        jwtService.saveRefreshToken(jwtTokenDto.getRefreshToken(), authentication);
        return jwtTokenDto;
    }

    @Override
    public void Logout(long memberId) {
        jwtService.deleteRefreshToken(memberId);
    }

    @Override
    public JwtTokenDto refresh(JwtTokenDto jwtTokenDto) throws JwtException {

        try {
            jwtService.getAuthenticationByAccessToken(jwtTokenDto.getAccessToken());
        } catch (ExpiredJwtException ignored) {
        }

        Authentication authentication = jwtService.getAuthenticationByRefreshToken(jwtTokenDto.getRefreshToken());
        JwtTokenDto newJwtTokenDto = jwtService.generateToken(authentication);
        jwtService.saveRefreshToken(newJwtTokenDto.getRefreshToken(), authentication);
        return newJwtTokenDto;

    }
}
