package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDtoTest {

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
        UserDto x = new UserDto(1, "Billy", "email55@yandex.com");
        UserDto y = new UserDto(1, "Billy", "email55@yandex.com");

        assertTrue(x.equals(y) && y.equals(x));
        assertEquals(x.hashCode(), y.hashCode());
    }
}
