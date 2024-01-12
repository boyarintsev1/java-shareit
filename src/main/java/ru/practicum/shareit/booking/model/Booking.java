package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Класс Booking содержит информацию о бронировании (booking) и содержит следующие поля:
 * id — уникальный идентификатор бронирования;
 * start — дата и время начала бронирования;
 * end — дата и время конца бронирования;
 * item — вещь, которую пользователь бронирует;
 * booker — пользователь, который осуществляет бронирование;
 * status — статус бронирования. Может принимать одно из следующих значений:
 * WAITING — новое бронирование, ожидает одобрения, APPROVED — бронирование подтверждено владельцем,
 * REJECTED — бронирование отклонено владельцем, CANCELED — бронирование отменено создателем
 */

@Data
public class Booking {
    private Long id;
    @NotNull
    @FutureOrPresent(message = "Дата бронирования не может быть из прошлого")
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent(message = "Дата бронирования не может быть из прошлого")
    private LocalDateTime end;
    private Item item;
    private User booker;
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
