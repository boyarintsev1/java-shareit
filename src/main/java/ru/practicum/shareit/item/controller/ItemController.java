package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-контроллер по Item
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(@Qualifier(value = "inMemoryItemService") ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    @GetMapping
    public List<ItemDto> findAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.findAllItems(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /**
     * метод получения данных о вещи по её ID
     */
    @GetMapping("/{id}")
    public ItemDto findItemById(@PathVariable("id") Long id) {
        return ItemMapper.toItemDto(itemService.findItemById(id));
    }

    /**
     * метод поиска вещи по наименованию и описанию
     */
    @GetMapping("/search")
    public List<ItemDto> findItem(@RequestParam(value = "text") String text) {
        return itemService.findItem(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /**
     * метод создания новой вещи
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @Valid @RequestBody Item item) {
        return ItemMapper.toItemDto(itemService.createItem(userId, item));
    }

    /**
     * метод обновления данных о вещи
     */
    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable("id") Long itemId,
                              @RequestBody Item item) {
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, item));
    }

    /**
     * метод удаления данных о вещи
     */
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);
    }
}

