package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;

public class IdExistsException extends RuntimeException {
    private final String parameter;
    HttpStatus status;

    public IdExistsException(String parameter, HttpStatus status) {
        this.parameter = parameter;
        this.status = status;
    }

    public String getParameter() {
        return parameter;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
