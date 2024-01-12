package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс-контроллер по Item
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final List<ItemDto>  itemDtoList = new ArrayList<>();

    private final List<ItemDto> itemsOfUser = new ArrayList<>();

    @Autowired
    public ItemController(@Qualifier(value = "inMemoryItemService") ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    @GetMapping
    public List<ItemDto> findAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        itemsOfUser.clear();
        for (Item i: itemService.findAllItems(userId)) {
            itemsOfUser.add(ItemMapper.toItemDto(i));
        }
        return itemsOfUser;
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
        itemDtoList.clear();
        for (Item i: itemService.findItem(text)) {
            itemDtoList.add(ItemMapper.toItemDto(i));
        }
        return itemDtoList;
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

