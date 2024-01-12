package ru.practicum.shareit.reviews.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Класс Review содержит информацию об отзывах (review) по бронированию и содержит следующие поля:
 * id — уникальный идентификатор отзыва;
 * creationTime — дата создания отзыва;
 * item — вещь, на которую пишут отзыв;
 * description - текст отзыва;
 * user — пользователь, написавший отзыв;
 * grade - оценка;
 */

@Data
public class Review {
    private Long id;
    @NotNull
    private LocalDateTime creationTime;
    private Item item;
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 500, message = "Размер описания не может превышать 500 символов")
    private String description;
    private User user;
    @NotNull
    @NotEmpty
    private Integer grade;

    public Review(Long id, LocalDateTime creationTime, Item item, String description, User user, Integer grade) {
        this.id = id;
        this.creationTime = creationTime;
        this.item = item;
        this.description = description;
        this.user = user;
        this.grade = grade;
    }
}
