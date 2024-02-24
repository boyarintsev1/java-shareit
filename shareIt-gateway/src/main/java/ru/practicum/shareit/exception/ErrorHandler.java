package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice(value = "ru.practicum.shareit")
@Slf4j
public class ErrorHandler {
    String message;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIncorrectIdException(final IncorrectIdException e) {
        if (e.getParameter().equals("ItemID")) {
            message = "в запросе неверно указана информация о вещи. Такой вещи не существует.";
        }
        if (e.getParameter().equals("UserID")) {
            message = "в заголовке запроса неверно указана информация. " +
                    "Пользователя с указанным ID не существует.";
        }
        if (e.getParameter().equals("BookingID")) {
            message = "в запросе неверно указана информация об бронировании. Бронирования с таким ID не существует.";
        }
        if (e.getParameter().equals("UserIsNotBooker")) {
            message = "данный пользователь не является автором заказа и не может его редактировать.";
        }
        if (e.getParameter().equals("RequestID")) {
            message = "в запросе неверно указана информация о запросе. Запроса на вещь с таким ID не существует.";
        }
        log.error(String.format("Неверно указан %s. Описание: %s", e.getParameter(), message));
        return new ErrorResponse(
                String.format("Неверно указан %s. Описание: %s", e.getParameter(), message));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestParamException(final RequestParamException e) {
        log.error(String.format(e.getParameter()));
        return new ErrorResponse(
                String.format(e.getParameter()));
    }

    @ExceptionHandler
    public ResponseEntity handleIdExistsException(final IdExistsException e) {
        return new ResponseEntity<>(
                String.format(e.getParameter()),
                e.getStatus()
        );
    }

    @ExceptionHandler
    public ResponseEntity handleValidationException(final ValidationException e) {
        return new ResponseEntity<>(
                String.format(e.getParameter()),
                e.getStatus()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        log.error("Ошибка валидации в следующих полях: {}", errors);
        return new ErrorResponse(
                String.format("Ошибка валидации в следующих полях: %s", errors));
}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.error("В заголовке отсутствует информация о владельце вещи.");
        return new ErrorResponse(
                "В заголовке отсутствует информация о владельце вещи.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        message = Objects.requireNonNull(e.getRootCause()).getMessage()
                .replaceAll("\n", ".")
                .replaceAll("\"", "");
        return new ErrorResponse(message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        message = e.getMessage()
                .substring(e.getMessage().lastIndexOf('.'))
                .replace(".","{")
                .replace(":", "}");
        return new ErrorResponse(String.format("Ошибка валидации данных в параметрах запроса: %s", message));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        e.printStackTrace();
        return new ErrorResponse(
                "Произошла непредвиденная ошибка"
        );
    }
}
