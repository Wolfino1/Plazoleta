package com.plazoleta.plazoleta.domain.exceptions;

public class MaxSizeExceededException extends RuntimeException {
    public MaxSizeExceededException(String message) {
        super(message);
    }
}
