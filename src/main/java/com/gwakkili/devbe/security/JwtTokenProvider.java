package com.gwakkili.devbe.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;

@Slf4j
public class JwtTokenProvider {

    private Key key;

    public JwtTokenProvider(@Value("{jwt.secret}") String secretKey){

    }

}
