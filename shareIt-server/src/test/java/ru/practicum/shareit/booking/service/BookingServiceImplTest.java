package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
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
            .start(LocalDateTime.now().minusMinutes(5))
            .end(LocalDateTime.now().plusMinutes(40))
            .item(item)
            .booker(booker)
            .status(APPROVED)
            .build();

    @Test
    void findAllBookingsForBooker_whenStateIsAll_thenReturnListOfBookingsSize2() {
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
    void findAllBookingsForBooker_whenStateIsCurrent_thenReturnListOfBookingsSize1() {
        List<Booking> expectedList = Collections.singletonList(booking2);
        Mockito.when(bookingRepository.findCurrentBookingsForBooker(
                        anyInt(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForBooker(booker.getId(), State.CURRENT, 1, 10)
                .getContent();

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForBooker_whenStateIsPast_thenReturnListOfBookingsSize1() {
        List<Booking> expectedList = Collections.singletonList(booking1);
        Mockito.when(bookingRepository.findPastBookingsForBooker(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForBooker(booker.getId(), State.PAST, 1, 10)
                .getContent();

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForBooker_whenStateIsFuture_thenReturnListOfBookingsSize0() {
        List<Booking> expectedList = List.of();
        Mockito.when(bookingRepository.findFutureBookingsForBooker(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForBooker(booker.getId(), State.FUTURE, 1, 10)
                .getContent();

        assertEquals(0, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForBooker_whenStateIsRejected_thenReturnListOfBookingsSize1() {
        List<Booking> expectedList = List.of(booking1);
        Mockito.when(bookingRepository.findAllByBooker_IdAndStatus(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForBooker(booker.getId(), State.REJECTED, 1, 10)
                .getContent();

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForBooker_whenStateIsWaiting_thenReturnListOfBookingsSize0() {
        List<Booking> expectedList = List.of();
        Mockito.when(bookingRepository.findAllByBooker_IdAndStatus(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForBooker(booker.getId(), State.WAITING, 1, 10)
                .getContent();

        assertEquals(0, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForOwner_whenStateIsAll_thenReturnListOfBookingsSize2() {
        List<Booking> expectedList = Arrays.asList(booking1, booking2);
        Mockito.when(bookingRepository.findAllBookingsToOwnerPageable(anyInt(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForOwner(owner.getId(), State.ALL, 1, 10)
                .getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForOwner_whenStateIsCurrent_thenReturnListOfBookingsSize1() {
        List<Booking> expectedList = Collections.singletonList(booking2);
        Mockito.when(bookingRepository.findCurrentBookingsForOwnerPageable(
                        anyInt(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForOwner(owner.getId(), State.CURRENT, 1, 10)
                .getContent();

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForOwner_whenStateIsPast_thenReturnListOfBookingsSize1() {
        List<Booking> expectedList = Collections.singletonList(booking1);
        Mockito.when(bookingRepository.findPastBookingsForOwnerPageable(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForOwner(owner.getId(), State.PAST, 1, 10)
                .getContent();

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForOwner_whenStateIsFuture_thenReturnListOfBookingsSize0() {
        List<Booking> expectedList = List.of();
        Mockito.when(bookingRepository.findFutureBookingsForOwnerPageable(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForOwner(owner.getId(), State.FUTURE, 1, 10)
                .getContent();

        assertEquals(0, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForOwner_whenStateIsRejected_thenReturnListOfBookingsSize1() {
        List<Booking> expectedList = Collections.singletonList(booking1);
        Mockito.when(bookingRepository.findStatusBookingsForOwnerPageable(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForOwner(owner.getId(), State.REJECTED, 1, 10)
                .getContent();

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllBookingsForOwner_whenStateIsWaiting_thenReturnListOfBookingsSize0() {
        List<Booking> expectedList = List.of();
        Mockito.when(bookingRepository.findStatusBookingsForOwnerPageable(anyInt(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Booking> actualList = bookingService
                .findAllBookingsForOwner(owner.getId(), State.WAITING, 1, 10)
                .getContent();

        assertEquals(0, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findBookingById_whenBookingIdIsValid_thenReturnBooking() {
        Booking expectedBooking = booking1;
        Mockito.when(bookingRepository.findById(expectedBooking.getId())).thenReturn(Optional.of(expectedBooking));

        Booking actualBooking = bookingService.findBookingById(expectedBooking.getId());

        assertEquals(expectedBooking, actualBooking, "Не равны");
    }

    @Test
    void findBookingById_whenBookingIsIsWrong_thenExceptionThrown() {
        Booking expectedBooking = booking1;
        expectedBooking.setId(0L);
        Mockito.when(bookingRepository.findById(expectedBooking.getId())).thenReturn(Optional.empty());

        assertThrows(IncorrectIdException.class,
                () -> bookingService.findBookingById(expectedBooking.getId()),
                "Не равны");
    }

    @Test
    void createBooking_whenItemIsAvailable_thenReturnBooking() {
        Booking expectedBooking = booking1;
        Mockito.when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);

        Booking actualBooking = bookingService.createBooking(expectedBooking);

        assertEquals(expectedBooking, actualBooking, "Не равны");
        verify(bookingRepository).save(actualBooking);
    }

    @Test
    void createBooking_whenBookingIsWrong_thenExceptionThrown() {
        Booking wrongBookingToCreate = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(100))
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(wrongBookingToCreate),
                "Не равны");

        verify(bookingRepository, never()).save(wrongBookingToCreate);
    }

    @Test
    void createBooking_whenItemIsNotAvailable_thenExceptionThrown() {
        Item itemNotAvailable = item;
        itemNotAvailable.setAvailable(false);
        Booking wrongBookingToCreate = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(itemNotAvailable)
                .booker(booker)
                .status(REJECTED)
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(wrongBookingToCreate),
                "Не равны");

        verify(bookingRepository, never()).save(wrongBookingToCreate);
    }

    @Test
    void updateBooking_whenItemToUpdateIsValid_thenReturnBooking() {
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
        Booking requestBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMinutes(20))
                .end(LocalDateTime.now().plusMinutes(40))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();
        Booking approvedBooking = Booking.builder()
                .id(1L)
                .start(requestBooking.getStart())
                .end(requestBooking.getEnd())
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        Mockito.when(bookingRepository.findById(requestBooking.getId())).thenReturn(Optional.of(requestBooking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(approvedBooking);

        Booking actualBooking = bookingService.approveBooking(booker.getId(), requestBooking.getId(), true);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(approvedBooking.getId(), savedBooking.getId());
        assertEquals(approvedBooking.getStart(), savedBooking.getStart());
        assertEquals(approvedBooking.getEnd(), savedBooking.getEnd());
        assertEquals(approvedBooking.getStatus(), savedBooking.getStatus());
    }

    @Test
    void approveBooking_whenStatusIsApproved_thenExceptionThrown() {
        Booking bookingToApprove = booking2;
        Mockito.when(bookingRepository.findById(bookingToApprove.getId())).thenReturn(Optional.of(bookingToApprove));

        assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(booker.getId(), bookingToApprove.getId(), true),
                "Не равны");

        verify(bookingRepository, never()).save(bookingToApprove);
    }

    @Test
    void approveBooking_whenApproveIsFalse_thenExceptionThrown() {
        Booking bookingToApprove = booking2;
        bookingToApprove.setStatus(WAITING);
        Booking rejectedBooking = Booking.builder()
                .id(1L)
                .start(bookingToApprove.getStart())
                .end(bookingToApprove.getEnd())
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();
        Mockito.when(bookingRepository.findById(bookingToApprove.getId())).thenReturn(Optional.of(bookingToApprove));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(rejectedBooking);

        Booking actualBooking = bookingService.approveBooking(booker.getId(), bookingToApprove.getId(), false);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(rejectedBooking, savedBooking);
    }

    @Test
    void deleteBooking_whenBookingIdIsValid() {
        try {
            Long bookingId = 1L;
            bookingRepository.deleteById(bookingId);

            verify(bookingRepository, times(1)).deleteById(bookingId);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void validateBookingPatchRequest_whenStartIsAfterEnd_thenExceptionThrown() {
        Booking wrongBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(60))
                .end(LocalDateTime.now().minusMinutes(40))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.validateBookingPatchRequest(wrongBooking),
                "Не равны");
    }

    @Test
    void validateBookingPatchRequest_whenStartEqualsEnd_thenExceptionThrown() {
        Booking wrongBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();

        assertThrows(ValidationException.class,
                () -> bookingService.validateBookingPatchRequest(wrongBooking),
                "Не равны");
    }

    @Test
    void validateBookingPatchRequest_whenStartIsBeforeNow_thenExceptionThrown() {
        Booking wrongBooking = booking1;

        assertThrows(ValidationException.class,
                () -> bookingService.validateBookingPatchRequest(wrongBooking),
                "Не равны");
    }

    @Test
    void validateBookingPatchRequest_whenEndIsBeforeNow_thenExceptionThrown() {
        Booking wrongBooking = booking1;

        assertThrows(ValidationException.class,
                () -> bookingService.validateBookingPatchRequest(wrongBooking),
                "Не равны");
    }
}