package com.plazoleta.plazoleta.infrastructure.exceptionshandler;

import com.plazoleta.plazoleta.domain.exceptions.EmptyException;
import com.plazoleta.plazoleta.domain.exceptions.MaxSizeExceededException;
import com.plazoleta.plazoleta.domain.exceptions.NullException;
import com.plazoleta.plazoleta.domain.exceptions.WrongArgumentException;
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
}
