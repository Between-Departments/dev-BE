package com.gwakkili.devbe.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@RedisHash(value = "refreshToken")
public class RefreshToken {
    @Id
    private String mail;

    private Set<Role> roles;

    private String token;

    @TimeToLive
    private long expiredTime;
}
