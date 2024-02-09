package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер по Item
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    @GetMapping
    public List<ItemDto> findAllItems(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        if (userService.findUserById(userId) == null)
            throw new IncorrectIdException("UserID");
        if ((from < 0) || (size <= 0))
            throw new ValidationException("неверно указаны параметры запросы from (д.б.>=0) или size (д.б.>0",
                    HttpStatus.BAD_REQUEST);
        return itemService.findAllItems(userId, from, size)
                .map(itemMapper::toItemDtoForOwner)
                .getContent();
    }

    /**
     * метод получения данных о вещи по её ID
     */
    @GetMapping("/{id}")
    public ItemDto findItemById(@RequestHeader(value = "X-Sharer-User-Id", required = false) Integer userId,
                                @PathVariable("id") Long id) {
        if ((userId != null) && (itemService.findItemById(id)).getOwner().getId().equals(userId)) {
            return itemMapper.toItemDtoForOwner(itemService.findItemById(id));
        }
        return itemMapper.toItemDtoForBooker(itemService.findItemById(id));
    }

    /**
     * метод поиска вещи по наименованию и описанию
     */
    @GetMapping("/search")
    public List<ItemDto> findItem(
            @RequestParam(value = "text") String text,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        if ((from < 0) || (size <= 0))
            throw new ValidationException("неверно указаны параметры запросы from (д.б.>=0) или size (д.б.>0",
                    HttpStatus.BAD_REQUEST);
        return itemService.findItem(text, from, size)
                .map(itemMapper::toItemDtoForBooker)
                .getContent();
    }

    /**
     * метод создания новой вещи
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @Valid @RequestBody ItemRequestDto requestDto) {
        Item item = itemMapper.toItem(requestDto);
        return (itemMapper.toItemDtoForOwner(itemService.createItem(userId, item)));
    }

    /**
     * метод обновления данных о вещи
     */
    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @PathVariable("id") Long itemId,
                              @RequestBody ItemRequestDto requestDto) {
        Item item = itemMapper.toItem(requestDto);
        return itemMapper.toItemDto(itemService.updateItem(userId, itemId, item));
    }

    /**
     * метод удаления данных о вещи
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);
    }

    /**
     * метод добавления комментария о вещи
     */
    @PostMapping("/{itemId}/comment") //POST /items/{itemId}/comment
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @PathVariable("itemId") Long itemId,
                                    @Valid @RequestBody CommentRequestDto requestDto) {
        if (userService.findUserById(userId) == null)
            throw new IncorrectIdException("UserID");
        if (itemService.checkUserBookedItemInPast(itemId, userId).isEmpty())
            throw new ValidationException("Пользователь не брал эту вещь в аренду. Комментарий оставить не получится.",
                    HttpStatus.BAD_REQUEST);
        Comment comment = commentMapper.toComment(
                userService.findUserById(userId),
                itemService.findItemById(itemId),
                requestDto);
        return commentMapper.toCommentDto(itemService.createComment(comment));
    }
}

