package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.exception.ErrorRequestException;
import dev.sepler.bytekeeper.rest.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = ErrorRequestException.class)
    public ResponseEntity<ErrorResponse> errorRequestException(final ErrorRequestException errorRequestException) {
        ErrorResponse errorResponse = new ErrorResponse().withMessage(errorRequestException.getMessage());
        return ResponseEntity.status(errorRequestException.getHttpStatus()).body(errorResponse);
    }

}
