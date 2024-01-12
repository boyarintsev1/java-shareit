package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * Класс ItemDto ("вещь") содержит описание вещи Item, которое будет возвращено пользователю. Поля:
   id — уникальный идентификатор вещи;
   name — краткое название;
   description — развёрнутое описание;
   available — статус о том, доступна или нет вещь для аренды;
   owner — владелец вещи;
   request — если вещь была создана по запросу другого пользователя, то в этом поле будет ID соответствующего запроса.
 */

public class ItemDto extends Item {

    private Integer request;

    public ItemDto(Long id, String name, String description, Boolean available, User owner, Integer request) {
        super(id, name, description, available, owner);
        this.request = request;
    }
}
