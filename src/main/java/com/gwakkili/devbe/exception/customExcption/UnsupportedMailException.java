package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;

public class UnsupportedMailException extends CustomException{
    public UnsupportedMailException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
