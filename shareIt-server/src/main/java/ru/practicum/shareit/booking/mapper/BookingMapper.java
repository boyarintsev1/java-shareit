package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

/**
 * Класс BookingMapper позволяет преобразовать бронирование Booking в нужный формат возврата данных BookingDto.
 */
@Component
@RequiredArgsConstructor
public class BookingMapper {
    public final ItemService itemService;
    public final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Booking toBooking(Integer userId, BookingRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return new Booking(
                requestDto.getId(),
                requestDto.getStart(),
                requestDto.getEnd(),
                itemService.findItemById(requestDto.getItemId()),
                userService.findUserById(userId),
                requestDto.getStatus() != null ? requestDto.getStatus() : BookingStatus.WAITING);
    }

    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemMapper.toItemShort(booking.getItem()),
                userMapper.toUserShort(booking.getBooker()),
                booking.getStatus());
    }

}

