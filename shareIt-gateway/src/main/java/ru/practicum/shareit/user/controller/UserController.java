package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Класс-контроллер по User, ответственный за валидацию входящих данных
 */
@RestController
@Slf4j
@Validated
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    /**
     * метод валидации перед получением списка всех пользователей
     */
    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        return userClient.findAllUsers();
    }

    /**
     * метод валидации перед получением данных о пользователе по его ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable("id") @Positive @NotNull Integer id) {
        return userClient.findUserById(id);
    }

    /**
     * метод валидации данных перед созданием нового пользователя
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Валидация входящих данных прошла успешно");
        return userClient.createUser(userRequestDto);
    }

    /**
     * метод валидации данных перед обновлением данных о пользователе
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestDto userRequestDto,
                              @PathVariable("id") @Positive @NotNull Integer id) {
        return userClient.updateUser(userRequestDto, id);
    }

    /**
     * метод валидации данных перед удалением данных о пользователе
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") @Positive @NotNull Integer id) {
        userClient.deleteUser(id);
    }
}