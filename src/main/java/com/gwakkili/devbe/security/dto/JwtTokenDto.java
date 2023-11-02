package com.gwakkili.devbe.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenDto {

    String accessToken;

    String refreshToken;
}
