package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.RequestService;

import java.util.stream.Collectors;

/**
 * Класс ItemMapper позволяет преобразовать вещь Item в нужный формат возврата данных ItemDto.
 */
@Component
@RequiredArgsConstructor
public class ItemMapper {
    public final ItemService itemService;
    public final RequestService requestService;
    public final CommentMapper commentMapper;


    public Item toItem(ItemRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return Item
                .builder()
                .id(requestDto.getId())
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .available(requestDto.getAvailable())
                .owner(requestDto.getOwner())
                .request(requestDto.getRequestId() != null ? requestService.findRequestById(requestDto.getRequestId()) : null)
                .build();
    }


    public ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public ItemShort toItemShort(Item item) {
        if (item == null) {
            return null;
        }
        return ItemShort
                .builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public ItemDto toItemDtoForOwner(Item item) {
        if (item == null) {
            return null;
        }
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .lastBooking(itemService.findLastBookingsOfItem(item.getId())
                        != null ? itemService.findLastBookingsOfItem(item.getId()) : null)
                .nextBooking(itemService.findNextBookingsOfItem(item.getId())
                        != null ? itemService.findNextBookingsOfItem(item.getId()) : null)
                .comments(itemService.findAllCommentsByItem_Id(item.getId())
                        .stream()
                        .map(commentMapper::toCommentDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public ItemDto toItemDtoForBooker(Item item) {
        if (item == null) {
            return null;
        }
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .lastBooking(null)
                .nextBooking(null)
                .comments(itemService.findAllCommentsByItem_Id(item.getId())
                        .stream()
                        .map(commentMapper::toCommentDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public ItemDto toItemDtoForRequest(Item item) {
        if (item == null) {
            return null;
        }
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .available(item.getAvailable())
                .build();
    }
}
