package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.entity.User;

import java.util.List;

/**
 * интерфейс для работы с данными о User
 */

public interface UserService {

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

    /**
     * метод валидации данных при PATCH-запросах
     */
    void validateUserPatchRequest(User user);
}
