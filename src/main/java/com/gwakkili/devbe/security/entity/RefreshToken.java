package com.gwakkili.devbe.security.entity;

import com.gwakkili.devbe.member.entity.Member;
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
    private long memberId;

    private String mail;

    private String nickname;

    private Set<Member.Role> roles;

    private String token;

    @TimeToLive
    private long expiredTime;
}
