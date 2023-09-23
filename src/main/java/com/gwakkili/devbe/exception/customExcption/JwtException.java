package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtException extends AuthenticationException {
    private final ExceptionCode exceptionCode;

    public JwtException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
