package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoTest {

    @Test
    void testHashCode() {
        CommentDto x = new CommentDto(
                1L, "Add comment from author", "Пушкин А.С.",
                LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0));

        CommentDto y = new CommentDto(
                1L, "Add comment from author", "Пушкин А.С.",
                LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0));

        assertTrue(x.equals(y) && y.equals(x));
        assertEquals(x.hashCode(), y.hashCode());
    }
}