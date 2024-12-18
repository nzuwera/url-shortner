package com.nzuwera.assignment.urlshortner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String exception) {
        super(exception);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
