package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-контроллер по User
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(@Qualifier(value = "inMemoryUserService") UserService userService) {
        this.userService = userService;
    }

    /**
     * метод получения списка всех пользователей
     */
    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    /**
     * метод получения данных о пользователе по его ID
     */
    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable("id") Integer id) {
        return (UserMapper.toUserDto(userService.findUserById(id)));
    }

    /**
     * метод создания нового пользователя
     */
    @PostMapping
    public UserDto createUser(@Valid @RequestBody User user) {
        return (UserMapper.toUserDto(userService.createUser(user)));
    }

    /**
     * метод обновления данных о пользователе
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody User user, @PathVariable("id") Integer id) {
        return (UserMapper.toUserDto(userService.updateUser(user, id)));
    }

    /**
     * метод удаления данных о пользователе
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }
}
