package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

/**
 * интерфейс для работы с данными о Item
 */
public interface ItemService {

    /**
     * метод получения списка всех вещей определенного пользователя
     */
    Page<Item> findAllItems(Integer userId, Integer from, Integer size);

    /**
     * метод получения списка всех вещей определенного владельца
     */
    List<Item> findAllItemsByOwnerAndRequest(Integer userId);

    /**
     * метод получения данных о вещи по её ID
     */
    Item findItemById(Long id);

    /**
     * метод поиска вещи по наименованию и описанию
     */
    Page<Item> findItem(String text, Integer from, Integer size);

    /**
     * метод создания новой вещи
     */
    Item createItem(Integer userId, Item item);

    /**
     * метод обновления данных о вещи
     */
    Item updateItem(Integer userId, Long itemId, Item item);

    /**
     * метод удаления данных о вещи
     */
    void deleteItem(Long id);

    /**
     * метод валидации данных о вещи при PATCH-запросах
     */
    void validateItemPatchRequest(Item item);

    /**
     * метод получения предыдущего бронирования указанной вещи
     */
    BookingDtoForOwner findLastBookingsOfItem(Long itemId);

    /**
     * метод получения следующего бронирования указанной вещи
     */
    BookingDtoForOwner findNextBookingsOfItem(Long itemId);

    /**
     * метод создания нового комментариев к указанной вещи
     */
    Comment createComment(Comment comment);

    /**
     * метод получения списка комментариев к указанной вещи
     */
    List<Comment> findAllCommentsByItem_Id(Long itemId);

    /**
     * метод проверки, что пользователь действительно брал вещь в прошлом.
     */
    List<Booking> checkUserBookedItemInPast(Long itemId, Integer userId);

}
