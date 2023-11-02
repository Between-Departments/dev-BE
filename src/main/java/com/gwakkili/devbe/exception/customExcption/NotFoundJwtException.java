package com.gwakkili.devbe.exception.customExcption;

import io.jsonwebtoken.JwtException;

public class NotFoundJwtException extends JwtException {
    public NotFoundJwtException(String message) {
        super(message);
    }
}
