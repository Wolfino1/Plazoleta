package com.plazoleta.plazoleta.infrastructure.exceptionshandler;

import com.plazoleta.plazoleta.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice

public class ControllerAdvisor {

    @ExceptionHandler(MaxSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> handleMaxSizeExceededException(MaxSizeExceededException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler(NullException.class)
    public ResponseEntity<ExceptionResponse> handleNullException(NullException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler(EmptyException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyException(EmptyException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler(WrongArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleWrongArgumentException(WrongArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPaginationException(InvalidPaginationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedAccessException(UnauthorizedAccessException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
    }
}
