package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.enums.State;

/**
 * интерфейс для работы с данными о Booking
 */
public interface BookingService {

    /**
     * метод получения списка бронирований текущего пользователя
     */
    Page<Booking> findAllBookingsForBooker(Integer bookerId, State stateEnum, Integer from, Integer size);

    /**
     * метод получения списка бронирований для всех вещей текущего пользователя
     */
    Page<Booking> findAllBookingsForOwner(Integer bookerId, State stateEnum, Integer from, Integer size);

    /**
     * метод получения данных о вещи по её ID
     */
    Booking findBookingById(Long id);

    /**
     * метод создания нового бронирования
     */
    Booking createBooking(Booking booking);

    /**
     * метод обновления данных о вещи
     */
    Booking updateBooking(Integer userId, Long itemId, Booking booking);

    /**
     * метод одобрения бронирования владельцем вещи
     */
    Booking approveBooking(Integer userId, Long bookingId, Boolean approved);

    /**
     * метод удаления данных о запросе
     */
    void deleteBooking(Long id);

    /**
     * метод валидации данных о бронировании при PATCH-запросах
     */
    void validateBookingPatchRequest(Booking booking);

}
