package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Класс Booking содержит информацию о бронировании (booking).
 */

@Data
public class Booking {

    /**
     * id — уникальный идентификатор бронирования;
     */
    private Long id;

    /**
     * start — дата и время начала бронирования;
     */
    @NotNull
    @FutureOrPresent(message = "Дата бронирования не может быть из прошлого")
    private LocalDateTime start;

    /**
     * end — дата и время конца бронирования;
     */
    @NotNull
    @FutureOrPresent(message = "Дата бронирования не может быть из прошлого")
    private LocalDateTime end;

    /**
     * item — вещь, которую пользователь бронирует;
     */
    private Item item;

    /**
     * booker — пользователь User, который осуществляет бронирование;
     */
    private User booker;

    /**
     * status — статус бронирования.
     */
    private BookingStatus status;

    public Booking(Long id, LocalDateTime start, LocalDateTime end, Item item, User booker, BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}
