package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * класс хранения и обработки данных о пользователях User в памяти
 */
@Component
@Qualifier("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    final Map<Integer, User> users = new HashMap<>();
    int id = 0;
    String message;

    /**
     * метод получения данных о всех пользователях в виде HashMap
     */
    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    /**
     * метод получения списка всех пользователей
     */
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * метод получения данных о пользователе по его ID
     */
    @Override
    public User findUserById(Integer id) {
        if (!users.containsKey(id)) {
            throw new IncorrectIdException("UserID");
        }
        return users.get(id);
    }

    /**
     * метод создания нового пользователя
     */
    @Override
    public User createUser(User user) {
        if (users.containsValue(user)) {
            message = String.format("Пользователь с электронной почтой %s уже зарегистрирован в системе. " +
                    "Его нельзя создать. Можно только обновить данные (метод PUT).", user.getEmail());
            log.error(message);
            throw new ValidationException(message, HttpStatus.CONFLICT);
        }
        id = id + 1;
        user.setId(id);
        log.info("Будет сохранен объект: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    /**
     * метод обновления данных о пользователе
     */
    @Override
    public User updateUser(User user, Integer id) {
        if (!users.containsKey(id)) {
            throw new IncorrectIdException("userNotExists");
        } else {
            if ((user.getName() != null) && (!user.getName().isBlank())) {
                users.get(id).setName(user.getName());
            }
            if (user.getEmail() != null) {
                if ((!users.containsValue(user)) || (user.getEmail().equals(users.get(id).getEmail()))) {
                    users.get(id).setEmail(user.getEmail());
                } else {
                    message = String.format("Пользователь с электронной почтой %s уже зарегистрирован в системе. " +
                            "Его нельзя создать. Можно только обновить данные (метод PUT).", user.getEmail());
                    log.error(message);
                    throw new ValidationException(message, HttpStatus.CONFLICT);
                }
            }
            log.info("Обновлен объект: {}", users.get(id));
            return UserMapper.toUserDto(users.get(id));
        }
    }

    /**
     * метод удаления данных о пользователе
     */
    @Override
    public void deleteUser(Integer id) {
        if (!users.containsKey(id)) {
            throw new IncorrectIdException("userNotExists");
        } else {
            users.remove(id);
        }
    }
}




