package com.gwakkili.devbe.security.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("jwt")
public class JwtProperties {

    private final String secretKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
}
