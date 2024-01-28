package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс CommentDto ("отзыв") содержит комментарий к вещи Item, который будет возвращен пользователю.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}

