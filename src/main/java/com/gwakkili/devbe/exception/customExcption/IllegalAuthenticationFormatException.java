package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;
import org.springframework.security.core.AuthenticationException;

public class IllegalAuthenticationFormatException extends AuthenticationException {

    private final ExceptionCode exceptionCode;

    public IllegalAuthenticationFormatException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
