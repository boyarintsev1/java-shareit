package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.booking.enums.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.enums.BookingStatus.WAITING;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void addItems() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(Item.builder()
                .name("Дрель")
                .description("Простая дрель")
                .owner(userRepository.findAll().get(0))
                .available(true)
                .request(null)
                .build());
        itemRepository.save(Item.builder()
                .name("Отвертка")
                .description("Незаменимая вещь")
                .owner(userRepository.findAll().get(0))
                .available(true)
                .request(null)
                .build());
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusHours(5))
                .end(LocalDateTime.now().plusDays(1L))
                .item(itemRepository.findAll().get(0))
                .booker(userRepository.findAll().get(1))
                .status(WAITING)
                .build());
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .item(itemRepository.findAll().get(1))
                .booker(userRepository.findAll().get(1))
                .status(APPROVED)
                .build());
    }

    @Test
    void findAllByBookerId() {
        Page<Booking> newPage = bookingRepository.findAllByBookerId(userRepository.findAll().get(1).getId(),
                PageRequest.of(0, 100));

        assertEquals(2, newPage.getContent().size());
        assertEquals("Felix", newPage.getContent().get(0).getBooker().getName());
        assertEquals("Felix", newPage.getContent().get(1).getBooker().getName());
    }

    @Test
    void findAllByBooker_IdAndStatus() {
        Page<Booking> newPage = bookingRepository.findAllByBooker_IdAndStatus(userRepository.findAll().get(1).getId(),
                String.valueOf(WAITING), PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertEquals("Felix", newPage.getContent().get(0).getBooker().getName());
        assertEquals("WAITING", String.valueOf(newPage.getContent().get(0).getStatus()));
    }

    @Test
    void findPastBookingsForBooker() {
        Page<Booking> newPage = bookingRepository.findPastBookingsForBooker(userRepository.findAll().get(1).getId(),
                LocalDateTime.now(), PageRequest.of(0, 100));

        assertEquals(0, newPage.getContent().size());
    }

    @Test
    void findFutureBookingsForBooker() {
        Page<Booking> newPage = bookingRepository.findFutureBookingsForBooker(userRepository.findAll().get(1).getId(),
                LocalDateTime.now(), PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertTrue(LocalDateTime.now().isBefore(newPage.getContent().get(0).getStart()));
    }

    @Test
    void findCurrentBookingsForBooker() {
        Page<Booking> newPage = bookingRepository.findCurrentBookingsForBooker(userRepository.findAll().get(1).getId(),
                LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertTrue(LocalDateTime.now().isAfter(newPage.getContent().get(0).getStart()));
        assertTrue(LocalDateTime.now().isBefore(newPage.getContent().get(0).getEnd()));
    }

    @Test
    void findAllBookingsByItem_Id() {
        List<Booking> expectedList = bookingRepository.findAllBookingsByItem_Id(
                itemRepository.findAll().get(1).getId());

        assertEquals(1, expectedList.size());
        assertEquals("APPROVED", String.valueOf(expectedList.get(0).getStatus()));
    }

    @Test
    void findByItem_idAndBooker_idAndEnd_dateIsBefore() {
        List<Booking> expectedList = bookingRepository.findByItem_idAndBooker_idAndEnd_dateIsBefore(
                itemRepository.findAll().get(0).getId(), userRepository.findAll().get(0).getId(),
                LocalDateTime.now());

        assertEquals(0, expectedList.size());
    }

    @Test
    void findAllBookingsToOwnerPageable() {
        Page<Booking> newPage = bookingRepository.findAllBookingsToOwnerPageable(
                userRepository.findAll().get(0).getId(),
                PageRequest.of(0, 100));

        assertEquals(2, newPage.getContent().size());
        assertEquals("Дрель", newPage.getContent().get(1).getItem().getName());
    }

    @Test
    void findCurrentBookingsForOwnerPageable() {
        Page<Booking> newPage = bookingRepository.findCurrentBookingsForOwnerPageable(
                userRepository.findAll().get(0).getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertTrue(LocalDateTime.now().isAfter(newPage.getContent().get(0).getStart()));
        assertTrue(LocalDateTime.now().isBefore(newPage.getContent().get(0).getEnd()));
    }

    @Test
    void findPastBookingsForOwnerPageable() {
        Page<Booking> newPage = bookingRepository.findPastBookingsForOwnerPageable(
                userRepository.findAll().get(0).getId(),
                LocalDateTime.now(),
                PageRequest.of(0, 100));

        assertEquals(0, newPage.getContent().size());
    }

    @Test
    void findFutureBookingsForOwnerPageable() {
        Page<Booking> newPage = bookingRepository.findFutureBookingsForOwnerPageable(
                userRepository.findAll().get(0).getId(),
                LocalDateTime.now(), PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertTrue(LocalDateTime.now().isBefore(newPage.getContent().get(0).getStart()));
    }

    @Test
    void findStatusBookingsForOwnerPageable() {
        Page<Booking> newPage = bookingRepository.findStatusBookingsForOwnerPageable(
                userRepository.findAll().get(0).getId(),
                String.valueOf(WAITING),
                PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertEquals("WAITING", String.valueOf(newPage.getContent().get(0).getStatus()));
    }

    @AfterEach
    public void deleteItems() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}