package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * интерфейс для работы с данными о Item
 */
public interface ItemService {

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    List<Item> findAllItems(Integer userId);

    /**
     * метод получения данных о вещи по её ID
     */
    Item findItemById(Long id);

    /**
     * метод поиска вещи по наименованию и описанию
     */
    List<Item> findItem(String text);

    /**
     * метод создания новой вещи
     */
    Item createItem(Integer userId, Item item);

    /**
     * метод обновления данных о вещи
     */
    Item updateItem(Integer userId, Long itemId, Item item);

    /**
     * метод удаления данных о вещи
     */
    void deleteItem(Long id);

    /**
     * метод валидации данных о вещи при PATCH-запросах
     */
    void validateItemPatchRequest(Item item);
}
