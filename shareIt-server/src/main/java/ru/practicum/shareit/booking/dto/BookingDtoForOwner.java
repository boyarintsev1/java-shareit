package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Класс BookingDtoForOwner содержит информацию о заказе Booking, которое будет возвращено владельцу вещи.
 */
@Getter
@Builder
@AllArgsConstructor
public class BookingDtoForOwner {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer bookerId;
}

