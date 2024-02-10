package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDtoTest {
    ItemRequestDto itemRequestDto;

    @BeforeEach
    public void addItemRequestDto() {
        itemRequestDto = new ItemRequestDto(
                1L, "Дрель", "Простая дрель", true, new User(), null);
    }

    @Test
    void testHashCode() {
        ItemRequestDto x = ItemRequestDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
        ItemRequestDto y = ItemRequestDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
        assertTrue(x.equals(y) && y.equals(x));
        assertEquals(x.hashCode(), y.hashCode());
    }

    @Test
    void getId() {
        assertEquals(1L, itemRequestDto.getId());
    }

    @Test
    void getName() {
        assertEquals("Дрель", itemRequestDto.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Простая дрель", itemRequestDto.getDescription());
    }

    @Test
    void getAvailable() {
        assertEquals(true, itemRequestDto.getAvailable());
    }

    @Test
    void getOwner() {
        assertEquals(new User(), itemRequestDto.getOwner());
    }

    @Test
    void getRequestId() {
        assertNull(itemRequestDto.getRequestId());
    }

    @Test
    void setId() {
        itemRequestDto.setId(2L);
        assertEquals(2L, itemRequestDto.getId());
    }

    @Test
    void setName() {
        itemRequestDto.setName("Пряник");
        assertEquals("Пряник", itemRequestDto.getName());
    }

    @Test
    void setDescription() {
        itemRequestDto.setDescription("Вкусный");
        assertEquals("Вкусный", itemRequestDto.getDescription());
    }

    @Test
    void setAvailable() {
        itemRequestDto.setAvailable(false);
        assertEquals(false, itemRequestDto.getAvailable());
    }

    @Test
    void setOwner() {
        User newUser = new User();
        itemRequestDto.setOwner(newUser);
        assertEquals(newUser, itemRequestDto.getOwner());
    }

    @Test
    void setRequestId() {
        itemRequestDto.setRequestId(5L);
        assertEquals(5L, itemRequestDto.getRequestId());
    }

    @Test
    void builder() {
        ItemRequestDto testBuilder = ItemRequestDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
        assertEquals("Дрель", testBuilder.getName());
        assertEquals("Простая дрель", testBuilder.getDescription());
        assertEquals(true, testBuilder.getAvailable());
    }
}