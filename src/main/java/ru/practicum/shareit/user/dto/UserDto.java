package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(getId(), userDto.getId()) && Objects.equals(getName(), userDto.getName())
                && Objects.equals(getEmail(), userDto.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail());
    }
}

