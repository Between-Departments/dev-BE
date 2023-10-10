package com.gwakkili.devbe.exception.customExcption;

import com.gwakkili.devbe.exception.ExceptionCode;

public class AccessDeniedException extends CustomException {
    public AccessDeniedException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
