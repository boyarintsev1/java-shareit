package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
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
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    /**
     * метод получения списка всех пользователей
     */
    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers()
                .stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }

    /**
     * метод получения данных о пользователе по его ID
     */
    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable("id") Integer id) {
        return (mapper.toUserDto(userService.findUserById(id)));
    }

    /**
     * метод создания нового пользователя
     */
    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserRequestDto requestDto) {
        User user = mapper.toUser(requestDto);

        return (mapper.toUserDto(userService.createUser(user)));
    }

    /**
     * метод обновления данных о пользователе
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserRequestDto requestDto, @PathVariable("id") Integer id) {
        User user = mapper.toUser(requestDto);
        return (mapper.toUserDto(userService.updateUser(user, id)));
    }

    /**
     * метод удаления данных о пользователе
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
    }
}
