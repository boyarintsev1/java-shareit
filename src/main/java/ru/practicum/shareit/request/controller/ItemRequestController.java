package ru.practicum.shareit.request.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер по запросам на вещь ItemRequest
 */
@RestController
@RequestMapping("/requests")
public class ItemRequestController {

    /**
     * метод получения списка всех запросов
     */
    @GetMapping
    public List<ItemRequestDto> findAllItemRequests() {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод получения данных о запросе по его ID
     */
    @GetMapping("/{id}")
    public ItemRequestDto findItemRequestDtoById(@PathVariable("id") Integer id) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод поиска запроса по его описанию
     */
    @GetMapping("/search")
    public List<ItemRequestDto> findItemRequest(@RequestParam(value = "text") String text) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод создания нового запроса
     */
    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @Valid @RequestBody ItemRequest itemRequest) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод обновления данных запроса
     */
    @PatchMapping("/{id}")
    public ItemRequestDto updateRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @PathVariable("id") Integer itemRequestId,
                                        @RequestBody ItemRequest itemRequest) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод удаления данных о заказе
     */
    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }
}

