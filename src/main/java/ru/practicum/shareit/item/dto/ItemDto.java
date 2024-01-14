package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * Класс ItemDto ("вещь") содержит описание вещи Item, которое будет возвращено пользователю.
 */

public class ItemDto extends Item {

    private Integer request;

    public ItemDto(Long id, String name, String description, Boolean available, User owner, Integer request) {
        super(id, name, description, available, owner);
        this.request = request;
    }
}
