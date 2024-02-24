package ru.practicum.shareit.item.entity;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    User owner = new User(1, "Billy", "email55@yandex.com");
    Item x = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
    Item y = new Item(1L, "Дрель", "Простая дрель", true, owner, null);

    @Test
    void testEquals_returnTrue() {
        assertEquals(x, y);
    }

    @Test
    void testEquals_returnFalse() {
        assertNotEquals(owner, y);
    }


    @Test
    void testHashCode() {
        assertTrue(x.equals(y) && y.equals(x));
        assertEquals(x.hashCode(), y.hashCode());
    }
}