package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Map;

/**
 * класс для работы с данными о User в памяти
 */
@Service
@Slf4j
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public InMemoryUserService(@Qualifier(value = "inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * метод получения данных о всех пользователях в виде HashMap
     */
    @Override
    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    /**
     * метод получения списка всех пользователей
     */
    @Override
    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    /**
     * метод получения данных о пользователе по его ID
     */
    @Override
    public User findUserById(Integer id) {
        return userStorage.findUserById(id);
    }

    /**
     * метод создания нового пользователя
     */
    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    /**
     * метод обновления данных о пользователе
     */
    @Override
    public User updateUser(User user, Integer id) {
        validateUserPatchRequest(user);
        return userStorage.updateUser(user, id);
    }

    @Override
    public void deleteUser(@PathVariable("id") Integer id) {
        userStorage.deleteUser(id);
    }

    /**
     * метод валидации данных при PATCH-запросах
     */
    @Override
    public void validateUserPatchRequest(User user) {
        if (user.getEmail() != null && user.getEmail().indexOf('@') == -1)
            throw new ValidationException("Электронная почта должна содержать символ @", HttpStatus.BAD_REQUEST);
    }
}

