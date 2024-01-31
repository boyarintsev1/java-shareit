package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * класс для работы с данными о вещи Item при помощи репозитория
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Item> findAllItems(Integer userId) {
        log.info("Выполняется запрос на получение всех вещей определенного пользователя.");
        return itemRepository.findByOwnerIdOrderByIdAsc(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Item findItemById(Long id) {
        log.info("Выполняется запрос на получения данных о вещи по ID: {}", id);
        return itemRepository.findById(id).orElseThrow(() -> new IncorrectIdException("ItemID"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Item> findItem(String text) {
        log.info("Выполняется поиска вещи по наименованию и описанию");
        if (text != null && !text.isBlank() && !text.isEmpty()) {
            return itemRepository.findInNameAndDescription(text.toLowerCase().trim());
        }
        return List.of();
    }

    @Transactional
    @Override
    public Item createItem(Integer userId, Item item) {
        if (userService.findUserById(userId) == null) {
            throw new IncorrectIdException("UserID");
        }
        item.setOwner(userService.findUserById(userId));
        log.info("Создан объект: {}", item);
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item updateItem(Integer userId, Long itemId, Item item) {
        validateItemPatchRequest(item);
        Item dbItem = findItemById(itemId);

        if (!userId.equals(dbItem.getOwner().getId())) {
            throw new IncorrectIdException("UserID");
        }
        if (item.getName() != null) {
            dbItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            dbItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            dbItem.setAvailable(item.getAvailable());
        }
        if (item.getOwner() != null) {
            dbItem.setOwner(item.getOwner());
        }
        if (item.getRequest() != null) {
            dbItem.setRequest(item.getRequest());
        }
        log.info("Обновлен объект: {}", dbItem);
        return itemRepository.save(dbItem);
    }

    @Transactional
    @Override
    public void deleteItem(Long id) {
        log.info("Удалена вещь с ID: {}", id);
        itemRepository.deleteById(id);
    }

    @Override
    public void validateItemPatchRequest(Item item) {
        if ((item.getName() != null) && (item.getName().trim().length() < 1 || item.getName().trim().length() > 30))
            throw new ValidationException("Название вещи должно быть длиной не более 30 символов",
                    HttpStatus.BAD_REQUEST);
        if ((item.getDescription() != null) &&
                (item.getDescription().trim().length() < 1 || item.getDescription().trim().length() > 200))
            throw new ValidationException("Размер описания не может превышать 200 символов", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public BookingDtoForOwner findLastBookingsOfItem(Long itemId) {
        Booking lastBooking = null;
        LocalDateTime min = LocalDateTime.now().minus(1000, ChronoUnit.YEARS);
        List<Booking> list = bookingRepository.findAllBookingsByItem_Id(itemId);
        for (Booking b : list) {
            if (b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now())) {
                lastBooking = b;
            }
            if ((b.getEnd().isAfter(min)) && (b.getEnd().isBefore(LocalDateTime.now()))) {
                min = b.getEnd();
                lastBooking = b;
            }
        }
        if (lastBooking == null) {
            return null;
        } else {
            return new BookingDtoForOwner(
                    lastBooking.getId(),
                    lastBooking.getStart(),
                    lastBooking.getEnd(),
                    lastBooking.getBooker().getId());
        }
    }

    @Transactional
    @Override
    public BookingDtoForOwner findNextBookingsOfItem(Long itemId) {
        Booking nextBooking = null;
        LocalDateTime max = LocalDateTime.now().plus(1000, ChronoUnit.YEARS);
        List<Booking> list = bookingRepository.findAllBookingsByItem_Id(itemId);
        for (Booking b : list) {
            if ((b.getStart().isBefore(max)) && (b.getStart().isAfter(LocalDateTime.now()))) {
                max = b.getStart();
                nextBooking = b;
            }
        }
        if (nextBooking == null) {
            return null;
        } else {
            return new BookingDtoForOwner(
                    nextBooking.getId(),
                    nextBooking.getStart(),
                    nextBooking.getEnd(),
                    nextBooking.getBooker().getId());
        }
    }

    @Override
    public Comment createComment(Comment comment) {
        log.info("Создан новый комментарий: {}", comment);
        return commentRepository.save(comment);

    }

    @Override
    public List<Comment> findAllCommentsByItem_Id(Long itemId) {
        log.info("Выполняется запрос на получение всех комментариев определенного пользователя.");
        return commentRepository.findAllByItem_id(itemId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> checkUserBookedItemInPast(Long itemId, Integer userId) {
        return bookingRepository.findByItem_idAndBooker_idAndEnd_dateIsBefore(itemId, userId, LocalDateTime.now());
    }
}

