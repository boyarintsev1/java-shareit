package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import javax.validation.constraints.*;
import java.time.LocalDateTime;


/**
 * Класс CommentRequestDto содержит информацию c входящего POST/PATCH запроса о комментариях (comment).
 */
@Getter
@Setter
@JsonPropertyOrder({"id", "text", "item", "author", "created"})
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 1000, message = "Отзыв должен быть длиной не более 1000 символов")
    private String text;
    private Item item;
    private User author;
    @FutureOrPresent(message = "Дата комментария не может быть из прошлого")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
