package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

/**
 * Класс UserDto ("пользователь") содержит описание User, которое будет возвращено при запросах. Всего три поля:
 * id — уникальный идентификатор пользователя;
 * name — имя или логин пользователя;
 * email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
 */
public class UserDto extends User {
    public UserDto(Integer id, String name, String email) {
        super(id, name, email);
    }

    public UserDto(String name, String email) {
        super(name, email);
    }
}

