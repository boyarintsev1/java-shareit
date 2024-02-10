package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserShort {
    private Integer id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserShort)) return false;
        UserShort userShort = (UserShort) o;
        return Objects.equals(getId(), userShort.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
