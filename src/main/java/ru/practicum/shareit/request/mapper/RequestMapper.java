package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Класс RequestMapper позволяет преобразовать запрос Request в нужный формат возврата данных RequestDto.
 */
@Component
@RequiredArgsConstructor
public class RequestMapper {
    public final ItemService itemService;
    public final UserService userService;
    public final ItemMapper itemMapper;

    public Request toRequest(Integer userId, IncomingRequestDto incomingRequestDto) {
        if (incomingRequestDto == null) {
            return null;
        }
        return new Request(
                incomingRequestDto.getId(),
                incomingRequestDto.getDescription(),
                userService.findUserById(userId),
                LocalDateTime.now());
    }

    public RequestDto toRequestDto(Request request) {
        if (request == null) {
            return null;
        }
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                itemService.findAllItemsByOwnerAndRequest(request.getRequestor().getId())
                .stream()
                .map(itemMapper::toItemDtoForRequest)
                .collect(Collectors.toList()));
    }
}

