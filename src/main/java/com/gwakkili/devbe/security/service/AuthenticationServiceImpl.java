package com.gwakkili.devbe.security.service;

import com.gwakkili.devbe.exception.ExceptionCode;
import com.gwakkili.devbe.exception.customExcption.JwtException;
import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.LoginDto;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.entity.RefreshToken;
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
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        MemberDetails memberDetails = (MemberDetails) authenticate.getPrincipal();
        return jwtService.generateToken(memberDetails);
    }

    @Override
    public void Logout(MemberDetails memberDetails) {
        jwtService.deleteRefreshToken(memberDetails);
    }

    @Override
    public JwtTokenDto refresh(JwtTokenDto jwtTokenDto) {

        String accessToken = jwtService.resolveAccessToken(jwtTokenDto.getAccessToken());
        String refreshToken = jwtTokenDto.getRefreshToken();

        String accessTokenValidate = jwtService.validateToken(accessToken);
        String refreshTokenValidate = jwtService.validateToken(refreshToken);

        if (accessTokenValidate.equals("NOT_FOUND") || refreshTokenValidate.equals("NOT_FOUND")) {
            throw new JwtException(ExceptionCode.NOT_FOUND_TOKEN);
        } else if (accessTokenValidate.equals("INVALID") || refreshTokenValidate.equals("INVALID")) {
            throw new JwtException(ExceptionCode.INVALID_TOKEN);
        } else if (refreshTokenValidate.equals("EXPIRE")) {
            throw new JwtException(ExceptionCode.EXPIRED_TOKEN);
        }
        String mail = jwtService.getClaims(refreshToken).get("mail").toString();

        RefreshToken redisRefreshToken = jwtService.getRefreshToken(mail);
        if (redisRefreshToken == null || !redisRefreshToken.getToken().equals(refreshToken))
            throw new JwtException(ExceptionCode.INVALID_TOKEN);

        MemberDetails memberDetails = MemberDetails.builder()
                .mail(redisRefreshToken.getMail())
                .roles(redisRefreshToken.getRoles())
                .build();

        return jwtService.generateToken(memberDetails);
    }

}
