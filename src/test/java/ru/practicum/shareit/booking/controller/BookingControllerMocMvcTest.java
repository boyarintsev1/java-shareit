package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.booking.enums.BookingStatus.WAITING;

@WebMvcTest
class BookingControllerMocMvcTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private BookingMapper bookingMapper;
    @MockBean
    private RequestService requestService;
    @MockBean
    private RequestMapper requestMapper;

    private User createTestUser() {
        User user = new User();
        user.setId(1);
        return user;
    }

    private Item createTestItem() {
        return Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .owner(createTestUser())
                .available(true)
                .request(null)
                .build();
    }

    private BookingDto createTestBookingDto() {
        return BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(20))
                .item(itemMapper.toItemShort(createTestItem()))
                .booker(userMapper.toUserShort(createTestUser()))
                .status(WAITING)
                .build();
    }

    private BookingRequestDto createTestBookingRequestDto() {
        return BookingRequestDto.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(20))
                .itemId(1L)
                .build();
    }

    private Booking createTestBooking() {
        return Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(20))
                .item(createTestItem())
                .booker(createTestUser())
                .status(WAITING)
                .build();
    }

    @SneakyThrows
    @Test
    void findAllBookings_whenUserIdIsCorrect_thenReturnListLength1() {
        User user = createTestUser();
        Booking booking = createTestBooking();
        BookingDto bookingDto = createTestBookingDto();
        Mockito.when(userService.findUserById(anyInt())).thenReturn(user);
        Mockito.when(bookingService.findAllBookingsForBooker(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Mockito.when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        String result = mockMvc.perform(
                        get("/bookings")
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void findAllBookingsForOwner_whenOwnerIdIsCorrect_thenReturnListLength1() {
        User user = createTestUser();
        Booking booking = createTestBooking();
        BookingDto bookingDto = createTestBookingDto();
        Mockito.when(userService.findUserById(anyInt())).thenReturn(user);
        Mockito.when(bookingService.findAllBookingsForOwner(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Mockito.when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        String result = mockMvc.perform(
                        get("/bookings/owner")
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void findBookingById_whenBookingIdIsValid_thenReturnBookingAndStatus200() {
        User user = createTestUser();
        Booking booking = createTestBooking();
        BookingDto bookingDto = createTestBookingDto();

        Mockito.when(userService.findUserById(anyInt())).thenReturn(user);
        Mockito.when(bookingService.findBookingById(anyLong())).thenReturn(booking);
        Mockito.when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        String result = mockMvc.perform(
                        get("/bookings/{bookingId}", booking.getId())
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void createBooking_whenBookingRequestDtoIsValid_thenReturnBookingAndStatus200() {
        User user = createTestUser();
        user.setId(2);
        BookingRequestDto requestDto = createTestBookingRequestDto();
        BookingDto bookingDto = createTestBookingDto();
        Booking booking = createTestBooking();
        Mockito.when(userService.findUserById(anyInt())).thenReturn(user);
        Mockito.when(bookingMapper.toBooking(anyInt(), Mockito.any())).thenReturn(booking);
        Mockito.when(bookingMapper.toBookingDto(bookingService.createBooking(Mockito.any()))).thenReturn(bookingDto);

        String result = mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", user.getId())
                                // .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void updateOrApproveBooking_whenApproveIsNull_thenReturnUpdate() {
        User user = createTestUser();
        BookingRequestDto requestDto = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(20))
                .itemId(2L)
                .build();
        BookingDto bookingDto = createTestBookingDto();
        Booking booking = createTestBooking();
        Mockito.when(userService.findUserById(anyInt())).thenReturn(user);
        Mockito.when(bookingService.findBookingById(anyLong())).thenReturn(booking);
        Mockito.when(bookingMapper.toBookingDto(bookingService.updateBooking(anyInt(), anyLong(), Mockito.any()))).thenReturn(bookingDto);

        String result = mockMvc.perform(
                        patch("/bookings/{bookingId}", booking.getId())
                                .header("X-Sharer-User-Id", user.getId())
                                // .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);

    }

    @SneakyThrows
    @Test
    void deleteBooking_whenBookingIdIsCorrect_thenReturnStatus200() {
        Long bookingId = 1L;

        mockMvc.perform(
                        delete("/bookings/{id}", bookingId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookingService, times(1)).deleteBooking(Mockito.any());
    }
}