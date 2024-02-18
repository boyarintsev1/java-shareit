package ru.practicum.shareit.request.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void findAllRequestsByUserId_whenUserIsValid_thenReturnListLength2() {
        User requestor = userService.createUser(new User(null, "Billy", "email55@yandex.com"));

        Request request1 = requestService.createRequest(Request.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build());

        Request request2 = requestService.createRequest(Request.builder()
                .description("Нужна модель Солнечной системы в масштабе 1:1")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build());

        mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", requestor.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].description").value(request2.getDescription()))
                .andExpect(jsonPath("$.[1].description").value(request1.getDescription()));
    }

    @SneakyThrows
    @Test
    void findAllRequestsByUserId_whenUserIsNotValid_thenExceptionThrows() {
        User requestor = userService.createUser(new User(null, "Billy", "email55@yandex.com"));

        Request request1 = requestService.createRequest(Request.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build());

        Request request2 = requestService.createRequest(Request.builder()
                .description("Нужна модель Солнечной системы в масштабе 1:1")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build());

        mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", 5)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(
                        IncorrectIdException.class, Objects.requireNonNull(result.getResolvedException()).getClass()));
    }
}
