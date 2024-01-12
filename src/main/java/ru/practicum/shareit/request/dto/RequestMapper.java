package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

/**
 * Класс RequestMapper позволяет преобразовать запрос ItemRequest в нужный формат возврата данных ItemRequestDto.
 */
public class RequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getRequestor(),
                itemRequest.getCreated()
        );
    }
}
