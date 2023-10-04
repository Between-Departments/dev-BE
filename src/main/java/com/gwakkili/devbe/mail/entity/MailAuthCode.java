package com.gwakkili.devbe.mail.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@RedisHash(value = "mailAuthToken")
public class MailAuthCode {

    @Id
    String mail;

    String authCode;

    boolean Auth;

    @TimeToLive
    private long expiredTime;

    public void setAuth(boolean auth) {
        Auth = auth;
    }
}
