package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;

public class AuthKeyExpireException extends CustomException{
    public AuthKeyExpireException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
