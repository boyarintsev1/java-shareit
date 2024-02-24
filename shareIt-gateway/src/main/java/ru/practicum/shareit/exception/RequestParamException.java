package ru.practicum.shareit.exception;

public class RequestParamException extends RuntimeException {
    private final String parameter;

    public RequestParamException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
