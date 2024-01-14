package ru.practicum.shareit.reviews.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.reviews.dto.ReviewDto;
import ru.practicum.shareit.reviews.model.Review;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер по отзывам Review
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    /**
     * метод получения списка всех отзывов от определенного пользователя
     */
    @GetMapping
    public List<ReviewDto> findAllReviews(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод получения данных об отзыве по его ID
     */
    @GetMapping("/{id}")
    public ReviewDto findReviewById(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод поиска отзыва по его тексту
     */
    @GetMapping("/search")
    public List<ReviewDto> findBooking(@RequestParam(value = "text") String text) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод создания нового отзыва
     */
    @PostMapping
    public ReviewDto createReview(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @Valid @RequestBody Review review) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод обновления данных отзыва
     */
    @PatchMapping("/{id}")
    public ReviewDto updateReview(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @PathVariable("id") Long reviewId,
                                  @RequestBody Review review) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }

    /**
     * метод удаления отзыва
     */
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }
}

