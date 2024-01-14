package ru.practicum.shareit.reviews.dto;

import ru.practicum.shareit.reviews.model.Review;

/**
 * Класс ItemMapper позволяет преобразовать вещь Item в нужный формат возврата данных ItemDto.
 */
public class ReviewMapper {
    public static ReviewDto toReviewDto(Review review) {
        return new ReviewDto(review.getId(),
                review.getCreationTime(), review.getItem(),
                review.getDescription(), review.getUser(), review.getGrade());
    }
}

