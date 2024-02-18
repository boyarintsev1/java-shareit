package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Класс-контроллер по Item, ответственный за валидацию входящих данных
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    /**
     * метод валидации входящих данных перед получением списка всех вещей определенного пользователя
     */
    @GetMapping
    public ResponseEntity<Object> findAllItems(
            @Positive @NotNull @RequestHeader("X-Sharer-User-Id")  Integer userId,
            @PositiveOrZero @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return itemClient.findAllItems(userId, from, size);
    }

    /**
     * метод валидации входящих данных перед получением данных о вещи по её ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findItemById(
            @RequestHeader(value = "X-Sharer-User-Id", required = false) Integer userId,
            @Positive @NotNull @PathVariable("id") Long itemId) {
        return itemClient.findItemById(userId, itemId);
    }

    /**
     * метод валидации входящих данных перед поиском вещи по наименованию и описанию
     */
    @GetMapping("/search")
    public ResponseEntity<Object> findItem(
            @Positive @NotNull @RequestHeader ("X-Sharer-User-Id") Integer userId,
            @NotNull @RequestParam (value = "text") String text,
            @PositiveOrZero @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return itemClient.findItem(userId, text, from, size);
    }

    /**
     * метод валидации входящих данных перед созданием новой вещи
     */
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemClient.createItem(userId, itemRequestDto);
    }

    /**
     * метод валидации входящих данных перед обновлением данных о вещи
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive @NotNull Integer userId,
                              @PathVariable("id") @Positive @NotNull Long itemId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    /**
     * метод валидации входящих данных перед удалением данных о вещи
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") @Positive @NotNull Long id) {
        itemClient.deleteItem(id);
    }

    /**
     * метод валидации входящих данных перед добавлением комментария о вещи
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") @Positive @NotNull Integer userId,
                                    @PathVariable("itemId") @Positive @NotNull Long itemId,
                                    @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.createComment(userId, itemId, commentRequestDto);
    }
}

