package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.user.dto.UserShort;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.booking.enums.BookingStatus.WAITING;

class BookingDtoTest {
    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
        BookingDto x = new BookingDto(
                1L,
                LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0),
                LocalDateTime.of(2023, 2, 10, 0, 0, 0, 0),
                new ItemShort(1L, "X"),
                new UserShort(2),
                WAITING);

        BookingDto y = new BookingDto(
                1L,
                LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0),
                LocalDateTime.of(2023, 2, 10, 0, 0, 0, 0),
                new ItemShort(1L, "X"),
                new UserShort(2),
                WAITING);

        assertTrue(x.equals(y) && y.equals(x));
        assertEquals(x.hashCode(), y.hashCode());
    }
}