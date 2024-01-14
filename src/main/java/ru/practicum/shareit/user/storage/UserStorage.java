package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

/**
 * интерфейс хранения данных о пользователях User
 */
public interface UserStorage {
    /**
     * метод получения данных о всех пользователях в виде HashMap
     */
    Map<Integer, User> getUsers();

    /**
     * метод получения списка всех пользователей
     */
    List<User> findAllUsers();

    /**
     * метод получения данных о пользователе по его ID
     */
    User findUserById(Integer id);

    /**
     * метод создания нового пользователя
     */
    User createUser(User user);

    /**
     * метод обновления данных о пользователе
     */
    User updateUser(User user, Integer id);

    /**
     * метод удаления данных о пользователе
     */
    void deleteUser(Integer id);
}
