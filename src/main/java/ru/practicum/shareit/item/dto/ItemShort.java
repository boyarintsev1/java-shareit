package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


/**
 * Класс ItemShort содержит краткое описание вещи Item, которое будет возвращено пользователю.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemShort {
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemShort)) return false;
        ItemShort itemShort = (ItemShort) o;
        return getId().equals(itemShort.getId()) && getName().equals(itemShort.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
