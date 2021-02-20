package dev.sepler.bytekeeper.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@EqualsAndHashCode(callSuper = true)
public class ErrorRequestException extends RuntimeException {

    HttpStatus httpStatus;

    public ErrorRequestException(final HttpStatus httpStatus, final String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
