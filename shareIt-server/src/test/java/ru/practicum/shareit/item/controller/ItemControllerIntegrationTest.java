package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
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
public class ItemControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void findAllItems_whenUserIdCorrect_thenStatus200() {
        User user = userService.createUser(new User(null, "Billy", "email55@yandex.com"));
        Item item1 = itemService.createItem(user.getId(),
                Item.builder()
                        .name("Дрель")
                        .description("Простая дрель")
                        .owner(user)
                        .available(true)
                        .request(null)
                        .build());
        Item item2 = itemService.createItem(user.getId(),
                Item.builder()
                        .name("Отвертка")
                        .description("Незаменимая вещь")
                        .owner(user)
                        .available(true)
                        .request(null)
                        .build());

        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].name").value(item1.getName()))
                .andExpect(jsonPath("$.[1].name").value(item2.getName()));
    }

    @SneakyThrows
    @Test
    void findAllItems_whenParamSizeIsCorrect_thenStatus400andExceptionThrown() {
        User user = userService.createUser(new User(null, "Billy", "email55@yandex.com"));
        Item item1 = itemService.createItem(user.getId(),
                Item.builder()
                        .name("Дрель")
                        .description("Простая дрель")
                        .owner(user)
                        .available(true)
                        .request(null)
                        .build());
        Item item2 = itemService.createItem(user.getId(),
                Item.builder()
                        .name("Отвертка")
                        .description("Незаменимая вещь")
                        .owner(user)
                        .available(true)
                        .request(null)
                        .build());

        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", user.getId())
                                .param("size", "0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(
                        ValidationException.class, Objects.requireNonNull(result.getResolvedException()).getClass()));
    }
}