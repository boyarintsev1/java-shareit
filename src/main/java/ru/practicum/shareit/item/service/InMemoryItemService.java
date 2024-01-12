package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

/**
 * класс для работы с данными о вещи Item в памяти
 */
@Service
@Slf4j
public class InMemoryItemService implements ItemService {
    private final ItemStorage itemStorage;

    @Autowired
    public InMemoryItemService(@Qualifier(value = "inMemoryItemStorage") ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    @Override
    public List<Item> findAllItems(Integer userId) {
        return itemStorage.findAllItems(userId);
    }

    /**
     * метод получения данных о вещи по её ID
     */
    @Override
    public Item findItemById(Long id) {
        return itemStorage.findItemById(id);
    }

    /**
     * метод поиска вещи по наименованию и описанию
     */
    @Override
    public List<Item> findItem(String text) {
        return itemStorage.findItem(text);
    }

    /**
     * метод создания новой вещи
     */
    @Override
    public Item createItem(Integer userId, Item item) {
        return itemStorage.createItem(userId, item);
    }

    /**
     * метод обновления данных о вещи
     */
    @Override
    public Item updateItem(Integer userId, Long itemId, Item item) {
        validateItemPatchRequest(item);
        return itemStorage.updateItem(userId, itemId, item);
    }

    /**
     * метод удаления данных о вещи
     */
    @Override
    public void deleteItem(Long id) {
        itemStorage.deleteItem(id);
    }

    /**
     * метод валидации данных о вещи при PATCH-запросах
     */
    @Override
    public void validateItemPatchRequest(Item item) {
        if ((item.getName() != null) && (item.getName().trim().length() < 1 || item.getName().trim().length() > 30))
            throw new ValidationException("Название вещи должно быть длиной не более 30 символов",
                    HttpStatus.BAD_REQUEST);
        if ((item.getDescription() != null) &&
                (item.getDescription().trim().length() < 1 || item.getDescription().trim().length() > 200))
            throw new ValidationException("Размер описания не может превышать 200 символов", HttpStatus.BAD_REQUEST);
    }
}

