package com.gwakkili.devbe.security.controller;

import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.LoginDto;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    //@Value("${jwt.refresh_token_expire_time}")
    //private final long refreshTokenExpireTime;

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        JwtTokenDto jwtTokenDto = authenticationService.login(loginDto);
        HttpHeaders headers = makeJwtHeader(jwtTokenDto);
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity logout(@AuthenticationPrincipal MemberDetails memberDetails) {
        authenticationService.Logout(memberDetails);

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", "")
                .path("/")
                .maxAge(1).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@CookieValue("RefreshToken") String refreshToken,
                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        JwtTokenDto jwtToken = authenticationService.refresh(new JwtTokenDto(accessToken, refreshToken));
        HttpHeaders headers = makeJwtHeader(jwtToken);
        return ResponseEntity.ok().headers(headers).build();
    }

    private HttpHeaders makeJwtHeader(JwtTokenDto jwtTokenDto) {
        HttpHeaders headers = new HttpHeaders();

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", jwtTokenDto.getRefreshToken())
                .path("/")
                .maxAge(10000)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenDto.getAccessToken());
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return headers;
    }

}
