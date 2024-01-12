package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

/**
 * интерфейс хранения данных о вещах Item
 */
public interface ItemStorage {

    /**
     * метод получения данных о всех вещах в виде HashMap
     */
    Map<Long, Item> getItems();

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    List<Item> findAllItems(Integer userId);

    /**
     * метод получения данных о вещи по её ID
     */
    Item findItemById(Long id);

    /**
     * метод поиска вещи
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
}
