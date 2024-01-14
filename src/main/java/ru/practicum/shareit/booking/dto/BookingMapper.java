package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

/**
 * Класс ItemMapper позволяет преобразовать вещь Item в нужный формат возврата данных ItemDto.
 */
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(),
                booking.getItem(), booking.getBooker(), booking.getStatus());
    }
}

