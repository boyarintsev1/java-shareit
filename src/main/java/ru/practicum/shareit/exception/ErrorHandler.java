package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(value = "ru.practicum.shareit")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIncorrectIdException(final IncorrectIdException e) {
        if (e.getParameter().equals("userNotExists")) {
            log.error("Пользователь с указанным ID ещё не зарегистрирован в системе. " +
                    "Сначала необходимо его создать (метод POST).");
            return new ErrorResponse("Пользователь с указанным ID ещё не зарегистрирован в системе. " +
                    "Сначала необходимо его создать (метод POST).");
        }
        if (e.getParameter().equals("itemNotExists")) {
            log.error("Вещь с указанным ID ещё не зарегистрирована в системе. Её нельзя обновить. " +
                    "Её нужно сначала создать (метод POST).");
            return new ErrorResponse("Вещь с указанным ID ещё не зарегистрирована в системе. Её нельзя обновить." +
                    "Её нужно сначала создать (метод POST).");
        }
        log.error("Неверно указан " + e.getParameter());
        return new ErrorResponse(
                String.format("Неверно указан %s.", e.getParameter()));
    }

    @ExceptionHandler
   // @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        e.printStackTrace();
        return new ErrorResponse(
                "Произошла непредвиденная ошибка."
        );
    }
}
