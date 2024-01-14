package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер по заказам Booking
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {

    /**
     * метод получения списка всех заказов определенного пользователя
     */
    @GetMapping
    public List<BookingDto> findAllBookings(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод получения данных о заказе по его ID
     */
    @GetMapping("/{id}")
    public BookingDto findBookingById(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод получения данных о всех заказах, поступивших владельцу вещи
     */
    @GetMapping("/owner/{id}")
    public BookingDto findBookingByOwner(@PathVariable("id") Integer ownerId) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод поиска заказа по его описанию
     */
    @GetMapping("/search")
    public List<BookingDto> findBooking(@RequestParam(value = "text") String text) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод создания нового заказа
     */
    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @Valid @RequestBody Booking booking) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод обновления данных заказа
     */
    @PatchMapping("/{id}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @PathVariable("id") Long bookingId,
                                    @RequestBody Booking booking) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод удаления данных о заказе
     */
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }
}

