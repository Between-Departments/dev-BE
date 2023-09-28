package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;

public class NotFoundException extends CustomException{
    public NotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
