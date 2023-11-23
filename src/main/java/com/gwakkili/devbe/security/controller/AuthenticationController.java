package com.gwakkili.devbe.security.controller;

import com.gwakkili.devbe.security.dto.JwtTokenDto;
import com.gwakkili.devbe.security.dto.LoginDto;
import com.gwakkili.devbe.security.dto.MemberDetails;
import com.gwakkili.devbe.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Authentication", description = "인증 API")
public class AuthenticationController {

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshTokenExpireTime;

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        JwtTokenDto jwtTokenDto = authenticationService.login(loginDto);
        HttpHeaders headers = makeJwtHeader(jwtTokenDto);
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "로그아웃")
    public ResponseEntity logout(@AuthenticationPrincipal MemberDetails memberDetails) {
        authenticationService.Logout(memberDetails.getMemberId());

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", "")
                .path("/")
                .maxAge(1).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    @Parameters({
            @Parameter(name = HttpHeaders.AUTHORIZATION, description = "accessToken", in = ParameterIn.HEADER),
            @Parameter(name = "RefreshToken", description = "refreshToken", in = ParameterIn.COOKIE)
    })
    public ResponseEntity refresh(@CookieValue(value = "RefreshToken", defaultValue = "") String refreshToken,
                                  @RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String authorization) {
        String accessToken = authorization.replace("Bearer ", "");
        JwtTokenDto jwtToken = authenticationService.refresh(new JwtTokenDto(accessToken, refreshToken));
        HttpHeaders headers = makeJwtHeader(jwtToken);
        return ResponseEntity.ok().headers(headers).build();
    }

    private HttpHeaders makeJwtHeader(JwtTokenDto jwtTokenDto) {
        HttpHeaders headers = new HttpHeaders();

        ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", jwtTokenDto.getRefreshToken())
                .path("/")
                .maxAge(refreshTokenExpireTime)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenDto.getAccessToken());
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return headers;
    }

}
