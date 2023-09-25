package com.gwakkili.devbe.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@RedisHash(value = "mailAuthToken")
public class MailAuthKey {

    @Id
    String mail;

    String authKey;

    boolean isAuth;

    @TimeToLive
    private long expiredTime;
}
