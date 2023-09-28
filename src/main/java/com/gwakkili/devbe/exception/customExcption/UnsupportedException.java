package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;

public class UnsupportedException extends CustomException{
    public UnsupportedException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
