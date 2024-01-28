package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.user.entity.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Класс BookingRequestDto содержит информацию c входящего POST/PATCH запроса о бронированиях (booking).
 */
@Getter
@Setter
@JsonPropertyOrder({"id", "start", "end", "item", "booker", "status"})
@AllArgsConstructor
public class BookingRequestDto {

    /**
     * id — уникальный идентификатор бронирования;
     */
    private Long id;

    /**
     * start — дата и время начала бронирования;
     */
    @NotNull
    @FutureOrPresent(message = "Дата бронирования не может быть из прошлого")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;

    /**
     * end — дата и время конца бронирования;
     */
    @NotNull
    @FutureOrPresent(message = "Дата бронирования не может быть из прошлого")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;

    /**
     * item — вещь, которую пользователь бронирует;
     */
    @NotNull
    private Long itemId;

    /**
     * booker — пользователь User, который осуществляет бронирование;
     */
    private User booker;

    /**
     * status — статус бронирования.
     */
    private BookingStatus status;
}
