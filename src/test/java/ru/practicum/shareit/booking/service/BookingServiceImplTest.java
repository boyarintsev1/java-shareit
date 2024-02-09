package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static ru.practicum.shareit.booking.enums.BookingStatus.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;
    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void findAllBookingsForBooker_whenBookerIsValid_thenReturnListOfBookingsSize2() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusMinutes(60))
                .end(LocalDateTime.now().minusMinutes(40))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        List<Booking> expectedList = Arrays.asList(booking1, booking2);
        Mockito.when(bookingRepository.findAllByBookerId(anyInt(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForBooker(booker.getId(), State.ALL, 1, 10)
                .getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForOwner_whenOwnerIsValid_thenReturnListOfBookingsSize2() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();
        Booking booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusMinutes(60))
                .end(LocalDateTime.now().minusMinutes(40))
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();
        List<Booking> expectedList = Arrays.asList(booking1, booking2);
        Mockito.when(bookingRepository.findStatusBookingsForOwnerPageable(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForOwner(owner.getId(), State.REJECTED, 1, 10)
                .getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findBookingById_whenBookingIdIsValid_thenReturnBooking() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking expectedBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();
        Mockito.when(bookingRepository.findById(expectedBooking.getId())).thenReturn(Optional.of(expectedBooking));

        Booking actualBooking = bookingService.findBookingById(expectedBooking.getId());

        assertEquals(expectedBooking, actualBooking, "Не равны");
    }

    @Test
    void createBooking_whenItemIsAvailable_thenReturnBooking() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking expectedBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();
        Mockito.when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);

        Booking actualBooking = bookingService.createBooking(expectedBooking);

        assertEquals(expectedBooking, actualBooking, "Не равны");
        verify(bookingRepository).save(actualBooking);
    }

    @Test
    void updateBooking_whenItemToUpdateIsValid_thenReturnBooking() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking oldBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMinutes(20))
                .end(LocalDateTime.now().plusMinutes(40))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();
        Booking newBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMinutes(60))
                .end(LocalDateTime.now().plusMinutes(80))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();
        Mockito.when(userService.findUserById(booker.getId())).thenReturn(booker);
        Mockito.when(bookingRepository.findById(oldBooking.getId())).thenReturn(Optional.of(oldBooking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(newBooking);

        Booking actualBooking = bookingService.updateBooking(booker.getId(), oldBooking.getId(), newBooking);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(newBooking.getId(), savedBooking.getId());
        assertEquals(newBooking.getStart(), savedBooking.getStart());
        assertEquals(newBooking.getEnd(), savedBooking.getEnd());
    }

    @Test
    void approveBooking_whenApproveIsTrue_thenReturnUpdatedBooking() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking requestBbooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMinutes(20))
                .end(LocalDateTime.now().plusMinutes(40))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();
        Booking approvedBooking = Booking.builder()
                .id(1L)
                .start(requestBbooking.getStart())
                .end(requestBbooking.getEnd())
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        Mockito.when(bookingRepository.findById(requestBbooking.getId())).thenReturn(Optional.of(requestBbooking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(approvedBooking);

        Booking actualBooking = bookingService.approveBooking(booker.getId(), requestBbooking.getId(), true);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(approvedBooking.getId(), savedBooking.getId());
        assertEquals(approvedBooking.getStart(), savedBooking.getStart());
        assertEquals(approvedBooking.getEnd(), savedBooking.getEnd());
        assertEquals(approvedBooking.getStatus(), savedBooking.getStatus());
    }

    @Test
    void deleteBooking_whenBookingIdIsValid() {
        Long bookingId = 1L;
        bookingRepository.deleteById(bookingId);

        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    void validateBookingPatchRequest_whenBookingIsNotValid_thenExceptionThrown() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(60))
                .end(LocalDateTime.now().minusMinutes(40))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.validateBookingPatchRequest(booking),
                "Не равны");
    }
}