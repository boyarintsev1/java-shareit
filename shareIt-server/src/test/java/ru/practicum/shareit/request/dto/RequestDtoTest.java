package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RequestDtoTest {
    RequestDto x = RequestDto.builder()
            .id(1L)
            .description("Хотел бы воспользоваться щёткой для обуви")
            .created(LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0))
            .items(null)
            .build();

    RequestDto o = RequestDto.builder()
            .id(1L)
            .description("Хотел бы воспользоваться щёткой для обуви")
            .created(LocalDateTime.of(2023, 1, 10, 0, 0, 0, 0))
            .items(null)
            .build();

    @Test
    void testEquals_whenThisEquals_thenReturnFalse() {
        assertTrue(x.equals(o));
    }

    @Test
    void testEquals_whenOIsNotInstance_thenReturnFalse() {
      //  assertFalse(!(y instanceof RequestDto));
        assertNotEquals(o instanceof RequestDto, false);

    }

    @Test
    void testHashCode() {
        assertTrue(x.equals(o) && o.equals(x));
        assertEquals(x.hashCode(), o.hashCode());
    }
}