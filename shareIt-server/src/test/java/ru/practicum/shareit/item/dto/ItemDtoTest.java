package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    @Test
    void testHashCode() {
        ItemDto x = new ItemDto(1L, "Дрель", "Простая дрель", true, 1,
                null, null, null, null);
        ItemDto y = new ItemDto(1L, "Дрель", "Простая дрель", true, 1,
                null, null, null, null);
        assertTrue(x.equals(y) && y.equals(x));
        assertEquals(x.hashCode(), y.hashCode());
    }
}