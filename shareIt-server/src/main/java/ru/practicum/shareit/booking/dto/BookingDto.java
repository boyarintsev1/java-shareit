package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.user.dto.UserShort;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс BookingDto ("заказ") содержит описание заказа Booking, которое будет возвращено пользователю.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemShort item;
    private UserShort booker;
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingDto)) return false;
        BookingDto that = (BookingDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getStart(), that.getStart())
                && Objects.equals(getEnd(), that.getEnd()) && Objects.equals(getItem(), that.getItem())
                && Objects.equals(getBooker(), that.getBooker()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStart(), getEnd(), getItem(), getBooker(), getStatus());
    }
}

