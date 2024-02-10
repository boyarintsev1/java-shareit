package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserShort;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static ru.practicum.shareit.booking.enums.BookingStatus.WAITING;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {
    @Mock
    public ItemService itemService;
    @Mock
    public UserService userService;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private BookingMapper bookingMapper;
    User owner = new User(1, "Billy", "email55@yandex.com");
    User booker = new User(2, "Felix", "felix@yandex.com");
    Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
    ItemShort itemShort = new ItemShort(1L, "Дрель");
    UserShort userShort = new UserShort(2);
    BookingRequestDto requestDto = BookingRequestDto
            .builder()
            .start(LocalDateTime.now().plusMinutes(10))
            .end(LocalDateTime.now().plusMinutes(20))
            .itemId(1L)
            .build();
    Booking booking = Booking.builder()
            .id(1L)
            .start(requestDto.getStart())
            .end(requestDto.getEnd())
            .item(item)
            .booker(booker)
            .status(WAITING)
            .build();
    BookingDto bookingDto = new BookingDto(1L, booking.getStart(), booking.getEnd(), itemShort, userShort, WAITING);

    @Test
    void toBooking_whenUserAndRequestDtoAreValid_thenReturnBooking() {
        Booking expectedBooking = Booking.builder()
                .start(requestDto.getStart())
                .end(requestDto.getEnd())
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();
        Mockito.when(itemService.findItemById(Mockito.any())).thenReturn(item);
        Mockito.when(userService.findUserById(anyInt())).thenReturn(booker);

        Booking actualBooking = bookingMapper.toBooking(booker.getId(), requestDto);

        assertEquals(expectedBooking, actualBooking, "Не равны");
    }

    @Test
    void toBooking_whenRequestDtoIsNull_thenReturnNull() {
        Booking actualBooking = bookingMapper.toBooking(booker.getId(), null);

        assertNull(actualBooking, "Не равны");
    }

    @Test
    void toBookingDto_whenBookingIsValid_thenReturnBookingDto() {
        BookingDto expectedBookingDto = bookingDto;
        Mockito.when(itemMapper.toItemShort(Mockito.any())).thenReturn(itemShort);
        Mockito.when(userMapper.toUserShort(Mockito.any())).thenReturn(userShort);

        BookingDto actualBookingDto = bookingMapper.toBookingDto(booking);

        assertEquals(expectedBookingDto, actualBookingDto, "Не равны");
    }

    @Test
    void toBookingDto_whenBookingIsNull_thenReturnNull() {
        BookingDto actualBookingDto = bookingMapper.toBookingDto(null);

        assertNull(actualBookingDto, "Не равны");
    }
}