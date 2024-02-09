package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.RequestParamException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Класс-контроллер по бронированиям Booking
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final  BookingService bookingService;
    public final BookingMapper bookingMapper;
    public final UserService userService;

    /**
     * метод получения списка бронирований текущего пользователя
     */
    @GetMapping
    public List<BookingDto> findAllBookings(
            @RequestHeader("X-Sharer-User-Id") Integer bookerId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        if (userService.findUserById(bookerId) == null)
            throw new IncorrectIdException("UserID");
        if ((from < 0) || (size <= 0))
            throw new ValidationException("неверно указаны параметры запросы from (д.б.>=0) или size (д.б.>0",
                    HttpStatus.BAD_REQUEST);

        if (Arrays.stream(State.values()).anyMatch(x -> Objects.equals(String.valueOf(x), state))) {
            return bookingService.findAllBookingsForBooker(bookerId, State.valueOf(state), from, size)
                    .map(bookingMapper::toBookingDto).getContent();
        } else {
            throw new RequestParamException("Unknown state: " + state);
        }
    }

    /**
     * метод получения списка бронирований для всех вещей текущего пользователя
     */
    @GetMapping("/owner")
    public List<BookingDto> findAllBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        if (userService.findUserById(ownerId) == null) {
            throw new IncorrectIdException("UserID");
        }
        if ((from < 0) || (size <= 0))
            throw new ValidationException("неверно указаны параметры запросы from (д.б.>=0) или size (д.б.>0",
                    HttpStatus.BAD_REQUEST);

        if (Arrays.stream(State.values()).anyMatch(x -> Objects.equals(String.valueOf(x), state))) {
            return bookingService.findAllBookingsForOwner(ownerId, State.valueOf(state), from, size)
                    .map(bookingMapper::toBookingDto).getContent();
        } else {
            throw new RequestParamException("Unknown state: " + state);
        }
    }

    /**
     * метод получения данных о заказе по его ID
     */
    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @PathVariable("bookingId") Long bookingId) {
        if (userService.findUserById(userId) == null)
            throw new IncorrectIdException("UserID");
        if (bookingService.findBookingById(bookingId).getItem().getOwner().getId().equals(userId) ||
                (bookingService.findBookingById(bookingId).getBooker().getId().equals(userId))) {
            return bookingMapper.toBookingDto(bookingService.findBookingById(bookingId));
        } else {
            throw new ValidationException("Информация о данном бронировании доступна только автору бронирования или " +
                    "владельцу вещи", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * метод создания нового заказа
     */
    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @Valid @RequestBody BookingRequestDto requestDto) {
        if (userService.findUserById(userId) == null)
            throw new IncorrectIdException("UserID");
        if (requestDto.getId() != null)
            throw new IncorrectIdException("Нельзя задавать ID");
        Booking booking = bookingMapper.toBooking(userId, requestDto);
        if (booking.getItem().getOwner().getId().equals(userId))
            throw new ValidationException("Пользователь не может бронировать свои вещи. Неверно указан ID.",
                    HttpStatus.NOT_FOUND);
        return bookingMapper.toBookingDto(bookingService.createBooking(booking));
    }

    /**
     * метод обновления данных заказа + метод одобрения бронирования владельцем вещи
     */
    @PatchMapping("/{bookingId}")
    public BookingDto updateOrApproveBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable("bookingId") Long bookingId,
                                             @RequestBody(required = false) BookingRequestDto requestDto,
                                             @RequestParam(value = "approved", required = false) Boolean approved) {
        if (userService.findUserById(userId) == null) {
            throw new IncorrectIdException("UserID");
        }
        if (approved != null) {
            if (!bookingService.findBookingById(bookingId).getItem().getOwner().getId().equals(userId)) {
                throw new IncorrectIdException("Данный пользователь не является владельцем вещи " +
                        "и не может её редактировать.");
            }
            return bookingMapper.toBookingDto(
                    bookingService.approveBooking(userId, bookingId, approved));
        }
        if (!bookingService.findBookingById(bookingId).getBooker().getId().equals(userId))
            throw new ValidationException("данный пользователь не является автором заказа и не может" +
                    " его редактировать.", HttpStatus.BAD_REQUEST);
        Booking booking = bookingMapper.toBooking(userId, requestDto);
        return bookingMapper.toBookingDto(bookingService.updateBooking(userId, bookingId, booking));
    }

    /**
     * метод удаления данных о заказе
     */
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteBooking(id);
    }
}

