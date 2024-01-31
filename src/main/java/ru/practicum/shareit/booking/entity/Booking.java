package ru.practicum.shareit.booking.entity;

import lombok.*;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс Booking содержит информацию о бронировании (booking).
 */
@Entity
@Table(schema = "public", name = "bookings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    /**
     * id — уникальный идентификатор бронирования;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * start — дата и время начала бронирования;
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    /**
     * end — дата и время конца бронирования;
     */
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    /**
     * item — вещь, которую пользователь бронирует;
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * booker — пользователь User, который осуществляет бронирование;
     */
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    /**
     * status — статус бронирования.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}
