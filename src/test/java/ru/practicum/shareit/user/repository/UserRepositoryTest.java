package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void addUsers() {
        userRepository.save(new User(1, "Billy", "email55@yandex.com"));
        userRepository.save(new User(2, "Felix", "felix@yandex.com"));
    }

    @Test
    void findByEmailContainingIgnoreCase_whenUpperCase_thenListSizeIsOne() {
        List<User> actualUsers = userRepository.findByEmailContainingIgnoreCase("Email55@yandex.com");

        assertEquals(1, actualUsers.size());
        assertEquals("Billy", actualUsers.get(0).getName());
    }

    @Test
    void findByEmailContainingIgnoreCase_whenEmailNotInDb_thenListSizeIsZero() {
        List<User> actualUsers = userRepository.findByEmailContainingIgnoreCase("rambler@yandex.com");

        assertEquals(0, actualUsers.size());
    }

    @AfterEach
    public void deleteUsers() {
        userRepository.deleteAll();
    }
}