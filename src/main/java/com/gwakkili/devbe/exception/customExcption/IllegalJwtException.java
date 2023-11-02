package com.gwakkili.devbe.exception.customExcption;

import io.jsonwebtoken.JwtException;

public class IllegalJwtException extends JwtException {
    public IllegalJwtException(String message) {
        super(message);
    }
}
