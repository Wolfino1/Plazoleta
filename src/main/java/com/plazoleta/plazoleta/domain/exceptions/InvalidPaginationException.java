package com.plazoleta.plazoleta.domain.exceptions;

public class InvalidPaginationException extends RuntimeException {
    public InvalidPaginationException(String message) {
        super(message);
    }
}
