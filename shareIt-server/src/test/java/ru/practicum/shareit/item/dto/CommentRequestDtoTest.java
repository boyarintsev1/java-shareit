package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentRequestDtoTest {

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
        CommentRequestDto x = CommentRequestDto.builder()
                .text("Add comment from author")
                .build();

        CommentRequestDto y = CommentRequestDto.builder()
                .text("Add comment from author")
                .build();

        assertTrue(x.equals(y) && y.equals(x));
        assertEquals(x.hashCode(), y.hashCode());
    }
}
