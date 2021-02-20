package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.exception.ErrorRequestException;
import dev.sepler.bytekeeper.rest.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = ErrorRequestException.class)
    public ResponseEntity<ErrorResponse> errorRequestException(final ErrorRequestException errorRequestException) {
        ErrorResponse errorResponse = new ErrorResponse().withMessage(errorRequestException.getMessage());
        return ResponseEntity.status(errorRequestException.getHttpStatus()).body(errorResponse);
    }

}
