package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

/**
 * класс для работы с данными о пользователе User при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    String message;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAllUsers() {
        log.info("Выполняется запрос на получение всех пользователей");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Integer id) {
        log.info("Выполняется запрос на получение пользователя по ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new IncorrectIdException("UserID"));
    }

    @Transactional
    @Override
    public User createUser(User user) {
        log.info("Создан объект: {}", user);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(User user, Integer id) {
        validateUserPatchRequest(user);
        User dbUser = findUserById(id);
        if ((user.getName() != null) && (!user.getName().isBlank())) {
            dbUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (((userRepository.findByEmailContainingIgnoreCase(user.getEmail())).isEmpty()) ||
                    (user.getEmail().equals(dbUser.getEmail()))) {
                dbUser.setEmail(user.getEmail());
            } else {
                message = String.format("Пользователь с электронной почтой %s уже зарегистрирован в системе. " +
                        "Его нельзя создать. Можно только обновить данные (метод PUT).", user.getEmail());
                log.error(message);
                throw new ValidationException(message, HttpStatus.CONFLICT);
            }
        }
        log.info("Обновлен объект: {}", dbUser);
        return userRepository.save(dbUser);
    }

    @Transactional
    @Override
    public void deleteUser(Integer id) {
        log.info("Удален пользователь с ID: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public void validateUserPatchRequest(User user) {
        if (user.getEmail() != null && user.getEmail().indexOf('@') == -1)
            throw new ValidationException("Электронная почта должна содержать символ @", HttpStatus.BAD_REQUEST);
    }
}
