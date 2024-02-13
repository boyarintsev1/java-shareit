package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return getId().equals(that.getId()) && getText().equals(that.getText())
                && getAuthorName().equals(that.getAuthorName()) && getCreated().equals(that.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getAuthorName(), getCreated());
    }
}

