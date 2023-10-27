package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;

public class DuplicateException extends CustomException {
    public DuplicateException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
