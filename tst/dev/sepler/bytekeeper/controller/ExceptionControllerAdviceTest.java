package dev.sepler.bytekeeper.controller;

import dev.sepler.bytekeeper.exception.ErrorRequestException;
import dev.sepler.bytekeeper.rest.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ExceptionControllerAdviceTest {

    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Test
    public void errorRequestException_worksOk() {
        String errorMessage = "errorMessage";
        ErrorRequestException errorRequestException = new ErrorRequestException(HttpStatus.BAD_REQUEST, errorMessage);

        ResponseEntity<ErrorResponse> response = exceptionControllerAdvice.errorRequestException(errorRequestException);

        Assertions.assertEquals(errorMessage, response.getBody().getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
