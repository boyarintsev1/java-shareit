package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.IdExistsException;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * класс хранения и обработки данных о вещах Item в памяти
 */
@Component
@Qualifier("inMemoryItemStorage")
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    long id = 0;
    String message;

    private final UserStorage userStorage;

    @Autowired
    public InMemoryItemStorage(@Qualifier(value = "inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * метод получения данных о всех вещах в виде HashMap
     */
    @Override
    public Map<Long, Item> getItems() {
        return items;
    }

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    public List<Item> findAllItems(Integer userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * метод получения данных о вещи по её ID
     */
    @Override
    public Item findItemById(Long id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new IncorrectIdException("ItemID");
        }
    }

    /**
     * метод поиска вещи
     */
    @Override
    public List<Item> findItem(String text) {
        return items.values()
                .stream()
                .filter(item -> ((text != null && !text.isBlank() && !text.isEmpty()) &&
                        (item.getAvailable()) &&
                        (item.getName().toLowerCase().contains(text.toLowerCase().trim()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase().trim()))))
                .collect(Collectors.toList());
    }

    /**
     * метод создания новой вещи
     */
    @Override
    public Item createItem(Integer userId, Item item) {
        if (!userStorage.getUsers().containsKey(userId)) {
            message = "В заголовке неверно указана информация о владельце вещи. Указанного пользователя не существует.";
            log.error(message);
            throw new IncorrectIdException("userNotExists");
        }
        if (items.containsValue(item)) {
            message = String.format("Вещь с названием: %s и описанием %s уже зарегистрирована в системе. " +
                            "Её нельзя создать. Можно только обновить данные (метод PUT).",
                    item.getName(), item.getDescription());
            log.error(message);
            throw new IdExistsException(message, HttpStatus.CONFLICT);
        }
        id = id + 1;
        item.setId(id);
        item.setOwner(userStorage.getUsers().get(userId));
        log.info("Будет сохранен объект: {}", item);
        items.put(item.getId(), item);
        return item;
    }

    /**
     * метод обновления данных о вещи
     */
    @Override
    public Item updateItem(Integer userId, Long itemId, Item item) {
        if (!items.containsKey(itemId)) {
            throw new IncorrectIdException("itemNotExists");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            message = "В заголовке неверно указана информация о владельце вещи. Указанного пользователя не существует.";
            log.error(message);
            throw new IncorrectIdException("userNotExists");
        }
        if (!userId.equals(items.get(itemId).getOwner().getId())) {
            message = "В заголовке неверно указана информация о владельце вещи.";
            log.error(message);
            throw new IncorrectIdException(message);
        }
        if (item.getId() == null) {
            item.setId(items.get(itemId).getId());
        }
        if (item.getName() == null) {
            item.setName(items.get(itemId).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(items.get(itemId).getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(items.get(itemId).getAvailable());
        }
        if (item.getOwner() == null) {
            item.setOwner(items.get(itemId).getOwner());
        }
        if (item.getRequest() == null) {
            item.setRequest(items.get(itemId).getRequest());
        }
        log.info("Обновлен объект: {}", item);
        items.put(item.getId(), item);
        return item;
    }

    /**
     * метод удаления данных о вещи
     */
    @Override
    public void deleteItem(Long id) {
        if (items.containsKey(id)) {
            items.remove(id);
        } else {
            throw new IncorrectIdException("itemNotExists");
        }
    }
}




