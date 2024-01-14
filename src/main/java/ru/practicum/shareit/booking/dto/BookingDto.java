package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс BookingDto ("заказ") содержит описание заказа Booking, которое будет возвращено пользователю.
 */

public class BookingDto extends Booking {
    public BookingDto(Long id,
                      LocalDateTime start,
                      LocalDateTime end,
                      Item item,
                      User booker,
                      BookingStatus status) {
        super(id, start, end, item, booker, status);
    }
}
