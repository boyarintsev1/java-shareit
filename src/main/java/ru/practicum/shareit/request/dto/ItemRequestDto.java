package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс ItemRequestDto содержит описание запроса ItemRequest, которое будет возвращено пользователю.
 */

public class ItemRequestDto extends ItemRequest {

    public ItemRequestDto(Integer id, String description, User requestor, LocalDateTime created) {
        super(id, description, requestor, created);
    }
}
