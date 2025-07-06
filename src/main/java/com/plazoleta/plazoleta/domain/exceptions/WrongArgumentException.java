package com.plazoleta.plazoleta.domain.exceptions;

public class WrongArgumentException extends RuntimeException {
    public WrongArgumentException(String message) {
        super(message);
    }
}
