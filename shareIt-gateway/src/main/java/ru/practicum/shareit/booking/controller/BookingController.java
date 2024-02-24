package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.RequestParamException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.Objects;

/**
 * Класс-контроллер по бронированиям Booking, ответственный за валидацию входящих данных
 */
@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
@Slf4j

public class BookingController {
    private final BookingClient bookingClient;

    /**
     * метод валидации входящих данных перед получением списка бронирований текущего пользователя
     */
    @GetMapping
    public ResponseEntity<Object> findAllBookings(
            @RequestHeader("X-Sharer-User-Id") Integer bookerId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        if (Arrays.stream(State.values()).anyMatch(x -> Objects.equals(String.valueOf(x), state))) {
            return bookingClient.findAllBookingsForBooker(bookerId, state, from, size);
        } else {
            throw new RequestParamException("Unknown state: " + state);
        }
    }

    /**
     * метод валидации входящих данных перед получением списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingsForOwner(
            @Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @PositiveOrZero @NotNull @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        if (Arrays.stream(State.values()).anyMatch(x -> Objects.equals(String.valueOf(x), state))) {
            return bookingClient.findAllBookingsForOwner(ownerId, state, from, size);
        } else {
            throw new RequestParamException("Unknown state: " + state);
        }
    }

    /**
     * метод валидации входящих данных перед получением данных о заказе по его ID
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @Positive @NotNull @PathVariable("bookingId") Long bookingId) {
        return bookingClient.findBookingById(userId, bookingId);
    }

    /**
     * метод валидации входящих данных перед созданием нового заказа
     */
    @PostMapping
    public ResponseEntity<Object> createBooking(
            @Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto.getId() != null)
            throw new IncorrectIdException("Нельзя задавать ID");
        return bookingClient.createBooking(userId, bookingRequestDto);
    }

    /**
     * метод валидации входящих данных перед обновлением данных заказа или перед одобрением бронирования владельцем вещи
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateOrApproveBooking(
            @Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Positive @NotNull @PathVariable("bookingId") Long bookingId,
            @RequestBody(required = false) BookingRequestDto bookingRequestDto,
            @RequestParam(value = "approved", required = false) Boolean approved) {
        return bookingClient.updateOrApproveBooking(userId, bookingId, bookingRequestDto, approved);
    }

    /**
     * метод валидации входящих данных перед удалением данных о заказе
     */
    @DeleteMapping("/{id}")
    public void deleteBooking(@Positive @NotNull @PathVariable("id") Long id) {
        bookingClient.deleteBooking(id);
    }
}
