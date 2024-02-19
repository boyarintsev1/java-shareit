package ru.practicum.shareit.booking.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.RequestParamException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
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
import static ru.practicum.shareit.booking.enums.BookingStatus.WAITING;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    @AfterEach
    public void resetDb() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void findAllBookingsForOwner() {
        User owner = userService.createUser(new User(null, "Billy", "email55@yandex.com"));
        User booker = userService.createUser(new User(null, "Felix", "felix@yandex.com"));
        Item item = itemService.createItem(owner.getId(),
                Item.builder()
                        .name("Дрель")
                        .description("Простая дрель")
                        .owner(owner)
                        .available(true)
                        .request(null)
                        .build());

        Booking booking = bookingService.createBooking(
                Booking.builder()
                        .start(LocalDateTime.now().plusMinutes(10))
                        .end(LocalDateTime.now().plusMinutes(20))
                        .item(item)
                        .booker(booker)
                        .status(WAITING)
                        .build());

        mockMvc.perform(
                        get("/bookings/owner")
                                .header("X-Sharer-User-Id", owner.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].item.name").value("Дрель"))
                .andExpect(jsonPath("$.[0].booker.id").value(2));
    }

    @SneakyThrows
    @Test
    void findAllBookingsForOwner_whenStateIsNotCorrect_thenThrowException() {
        User owner = userService.createUser(new User(null, "Billy", "email55@yandex.com"));
        User booker = userService.createUser(new User(null, "Felix", "felix@yandex.com"));
        Item item = itemService.createItem(owner.getId(),
                Item.builder()
                        .name("Дрель")
                        .description("Простая дрель")
                        .owner(owner)
                        .available(true)
                        .request(null)
                        .build());

        Booking booking = bookingService.createBooking(
                Booking.builder()
                        .start(LocalDateTime.now().plusMinutes(10))
                        .end(LocalDateTime.now().plusMinutes(20))
                        .item(item)
                        .booker(booker)
                        .status(WAITING)
                        .build());

        mockMvc.perform(
                        get("/bookings/owner")
                                .header("X-Sharer-User-Id", owner.getId())
                                .param("state", "INCORRECT")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(RequestParamException.class, Objects.requireNonNull(result.getResolvedException()).getClass()));
    }
}
