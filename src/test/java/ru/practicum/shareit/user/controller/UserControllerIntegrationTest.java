package ru.practicum.shareit.user.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @Test
    void findUserById_whenUserIdIsCorrect_thenReturnUserAndStatus200() {
        User user1 = new User(null, "Billy", "email55@yandex.com");
        userService.createUser(user1);
        User user2 = new User(null, "Felix", "felix@yandex.com");
        userService.createUser(user2);
        int k = userService.findAllUsers().size();
        Integer id = userService.findAllUsers().get(k - 1).getId();
        mockMvc.perform(
                        get("/users/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Felix"))
                .andExpect(jsonPath("$.email").value("felix@yandex.com"));
    }

    @SneakyThrows
    @Test
    void findUserById_whenUserIdIsNotValid_thenStatus404andExceptionThrown() {
        User user1 = new User(1, "Billy", "email55@yandex.com");
        userService.createUser(user1);
        User user2 = new User(2, "Felix", "felix@yandex.com");
        userService.createUser(user2);
        Integer id = userService.findAllUsers().size() + 1;
        mockMvc.perform(
                        get("/users/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(
                        IncorrectIdException.class, Objects.requireNonNull(result.getResolvedException()).getClass()));
    }

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
    }
}