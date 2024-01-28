package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс UserDto ("пользователь") содержит описание User, которое будет возвращено при запросах.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private String email;
}

