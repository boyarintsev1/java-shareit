package ru.practicum.shareit.reviews.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.reviews.model.Review;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Класс ReviewDto ("отзыв") содержит описание отзыва Review, которое будет возвращено пользователю.
 */

public class ReviewDto extends Review {

    public ReviewDto(Long id, LocalDateTime creationTime, Item item, String description, User user, Integer grade) {
        super(id, creationTime, item, description, user, grade);
    }

}
