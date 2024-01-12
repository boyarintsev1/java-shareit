package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс ItemRequestDto содержит описание запроса ItemRequest, которое будет возвращено пользователю. Поля::
 * id — уникальный идентификатор запроса;
 * description — текст запроса, содержащий описание требуемой вещи;
 * requestor — пользователь, создавший запрос;
 * created — дата и время создания запроса.
 */

public class ItemRequestDto extends ItemRequest {

    public ItemRequestDto(Integer id, String description, User requestor, LocalDateTime created) {
        super(id, description, requestor, created);
    }
}
