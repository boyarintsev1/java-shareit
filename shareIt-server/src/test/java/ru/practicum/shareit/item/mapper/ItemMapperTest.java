package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {
@Mock
    ItemService itemService;
    @Mock
    CommentMapper commentMapper;
    @InjectMocks
    ItemMapper itemMapper;

    @Test
    void toItem_whenRequestDtoIsNotNull_thenReturnItem() {
        Item expectedItem = new Item(
                null, "Дрель", "Простая дрель", true, null, null);
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        Item actualItem = itemMapper.toItem(requestDto);

        assertEquals(expectedItem, actualItem, "Не равны");
    }

    @Test
    void toItem_whenRequestDtoIsNull_thenReturnNull() {
        Item expectedItem = null;
        ItemRequestDto requestDto = null;

        Item actualItem = itemMapper.toItem(requestDto);

        assertEquals(expectedItem, actualItem, "Не равны");
    }

    @Test
    void toItemDto_whenItemIsNotNull_thenReturnItemDto() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        ItemDto expectedItemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, 1,
                null, null, null, null);

        ItemDto actualItemDto = itemMapper.toItemDto(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");
    }

    @Test
    void toItemDto_whenItemIsNull_thenReturnNull() {
        ItemDto expectedItemDto = null;
        Item item = null;

        ItemDto actualItemDto = itemMapper.toItemDto(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");
    }


    @Test
    void toItemShort_whenItemIsNotNull_thenReturnItemShort() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        ItemShort expectedItemShort = new ItemShort(1L, "Дрель");

        ItemShort actualItemShort = itemMapper.toItemShort(item);

        assertEquals(expectedItemShort, actualItemShort, "Не равны");
    }

    @Test
    void toItemShort_whenItemIsNull_thenReturnNull() {
        Item item = null;
        ItemShort expectedItemShort = null;

        ItemShort actualItemShort = itemMapper.toItemShort(item);

        assertEquals(expectedItemShort, actualItemShort, "Не равны");
    }

    @Test
    void toItemDtoForOwner_whenItemIsNotNull_thenReturnItemDto() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        BookingDtoForOwner lastBookingDtoForOwner = new BookingDtoForOwner(1L, LocalDateTime.now().minusMinutes(20),
                LocalDateTime.now().minusMinutes(10), 3);
        BookingDtoForOwner nextBookingDtoForOwner = new BookingDtoForOwner(1L, LocalDateTime.now().plusMinutes(20),
                LocalDateTime.now().plusMinutes(40), 3);
        Mockito.when(itemService.findLastBookingsOfItem(item.getId())).thenReturn(lastBookingDtoForOwner);
        Mockito.when(itemService.findNextBookingsOfItem(item.getId())).thenReturn(nextBookingDtoForOwner);
        Mockito.when(itemService.findAllCommentsByItem_Id(item.getId())).thenReturn(List.of());
        ItemDto expectedItemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, 1,
                null, lastBookingDtoForOwner, nextBookingDtoForOwner, List.of());

        ItemDto actualItemDto = itemMapper.toItemDtoForOwner(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");
    }

    @Test
    void toItemDtoForOwner_whenItemIsNull_thenReturnNull() {
        ItemDto expectedItemDto = null;
        Item item = null;

        ItemDto actualItemDto = itemMapper.toItemDtoForOwner(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");
    }

    @Test
    void toItemDtoForBooker_whenItemIsNotNull_thenReturnItemDto() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Mockito.when(itemService.findAllCommentsByItem_Id(item.getId())).thenReturn(List.of());
        ItemDto expectedItemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, 1,
                null, null, null, List.of());

        ItemDto actualItemDto = itemMapper.toItemDtoForBooker(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");
    }

    @Test
    void toItemDtoForBooker_whenItemIsNull_thenReturnNull() {
        ItemDto expectedItemDto = null;
        Item item = null;

        ItemDto actualItemDto = itemMapper.toItemDtoForOwner(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");
    }

    @Test
    void toItemDtoForRequest_whenItemIsNotNull_thenReturnItemDto() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        ItemDto expectedItemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, null,
                null, null, null, null);

        ItemDto actualItemDto = itemMapper.toItemDtoForRequest(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");

    }

    @Test
    void toItemDtoForRequest_whenItemIsNull_thenReturnNull() {
        ItemDto expectedItemDto = null;
        Item item = null;

        ItemDto actualItemDto = itemMapper.toItemDtoForRequest(item);

        assertEquals(expectedItemDto, actualItemDto, "Не равны");
    }
}




