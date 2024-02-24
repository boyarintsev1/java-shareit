package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

/**
 * Класс BookingServiceImpl содержит имплементацию интерфейса о бронировании (booking).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    String message;
    private final BookingRepository bookingRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    @Override
    public Page<Booking> findAllBookingsForBooker(Integer bookerId, State stateEnum, Integer from, Integer size) {
        log.info("Исполняется запрос на получение всех бронирований пользователя.");
        Pageable page = PageRequest.of(from / size, size);
        LocalDateTime now = LocalDateTime.now();
        switch (stateEnum) {
            case ALL:
                return bookingRepository.findAllByBookerId(bookerId, page);
            case CURRENT:
                return bookingRepository.findCurrentBookingsForBooker(bookerId, now, now, page);
            case PAST:
                return bookingRepository.findPastBookingsForBooker(bookerId, now, page);
            case FUTURE:
                return bookingRepository.findFutureBookingsForBooker(bookerId, now, page);
            case WAITING:
            case REJECTED:
                String status = String.valueOf(stateEnum);
                return bookingRepository.findAllByBooker_IdAndStatus(bookerId, status, page);
        }
        return null;
    }

    @Transactional
    @Override
    public Page<Booking> findAllBookingsForOwner(Integer ownerId, State stateEnum, Integer from, Integer size) {
        log.info("Исполняется запрос на получение всех бронирований для владельца.");
        Pageable page = PageRequest.of(from / size, size);
        LocalDateTime now = LocalDateTime.now();
        switch (stateEnum) {
            case ALL:
                return bookingRepository.findAllBookingsToOwnerPageable(ownerId, page);
            case CURRENT:
                return bookingRepository.findCurrentBookingsForOwnerPageable(ownerId, now, now, page);
            case PAST:
                return bookingRepository.findPastBookingsForOwnerPageable(ownerId, now, page);
            case FUTURE:
                return bookingRepository.findFutureBookingsForOwnerPageable(ownerId, now, page);
            case WAITING:
            case REJECTED:
                String status = String.valueOf(stateEnum);
                return bookingRepository.findStatusBookingsForOwnerPageable(ownerId, status, page);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Booking findBookingById(Long id) {
        log.info("Исполняется запрос на получение бронирования по его ID.");
        return bookingRepository.findById(id).orElseThrow(() -> new IncorrectIdException("BookingID"));
    }

    @Transactional
    @Override
    public Booking createBooking(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart()) || (booking.getEnd().equals(booking.getStart()))) {
            message = "Дата окончания бронирования не может быть ранее его начала или равна ему.";
            log.error(message);
            throw new ValidationException(message, HttpStatus.BAD_REQUEST);
        }
        if (!booking.getItem().getAvailable()) {
            message = "Данная вещь не может быть забронирована.";
            log.error(message);
            throw new ValidationException(message, HttpStatus.BAD_REQUEST);
        }
        log.info("Создано новое бронирование.");
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking updateBooking(Integer userId, Long bookingId, Booking booking) {
        validateBookingPatchRequest(booking);
        if (userService.findUserById(userId) == null) {
            throw new IncorrectIdException("UserID");
        }
        Booking dbBooking = findBookingById(bookingId);
        if (!dbBooking.getBooker().getId().equals(userId)) {
            throw new IncorrectIdException("UserIsNotBooker");
        }
        if (booking.getStart() != null) {
            dbBooking.setStart(booking.getStart());
        }
        if (booking.getEnd() != null) {
            dbBooking.setEnd(booking.getEnd());
        }
        if (booking.getItem() != null) {
            dbBooking.setItem(booking.getItem());
        }
        dbBooking.setStatus(BookingStatus.WAITING);
        log.info("Обновлено бронирование {}", dbBooking);
        return bookingRepository.save(dbBooking);
    }

    @Transactional
    @Override
    public Booking approveBooking(Integer userId, Long bookingId, Boolean approved) {
        Booking dbBooking = findBookingById(bookingId);
        if (dbBooking.getStatus().equals(BookingStatus.APPROVED))
            throw new ValidationException("Нельзя изменить статус подтвержденного заказа", HttpStatus.BAD_REQUEST);
        if (approved) {
            dbBooking.setStatus(BookingStatus.APPROVED);
            log.info("Одобрено бронирование с ID = " + bookingId);
        } else {
            dbBooking.setStatus(BookingStatus.REJECTED);
            log.info("Отклонено бронирование с ID = " + bookingId);
        }
        return bookingRepository.save(dbBooking);
    }

    @Transactional
    @Override
    public void deleteBooking(Long id) {
        log.info("Выполняется удаление бронирования.");
        bookingRepository.deleteById(id);
    }

    @Override
    public void validateBookingPatchRequest(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        if (booking.getEnd().isBefore(booking.getStart()) || (booking.getEnd().equals(booking.getStart()))) {
            message = "Дата окончания бронирования не может быть ранее его начала или равна ему.";
            log.error(message);
            throw new ValidationException(message, HttpStatus.BAD_REQUEST);
        }
        if (booking.getStart().isBefore(now) || (booking.getEnd().isBefore(now))) {
            message = "Дата бронирования не может быть из прошлого.";
            log.error(message);
            throw new ValidationException(message, HttpStatus.BAD_REQUEST);
        }
    }
}

