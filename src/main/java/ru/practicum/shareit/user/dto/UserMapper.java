package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

/**
 * Класс UserMapper позволяет преобразовать User в нужный формат возврата данных UserDto.
 */
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
