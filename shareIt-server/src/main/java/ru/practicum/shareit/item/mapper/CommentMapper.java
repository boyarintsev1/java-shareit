package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

/**
 * Класс CommentMapper позволяет преобразовать данные комментария Comment в нужный формат возврата данных CommentDto.
 */
@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment toComment(User userById, Item itemById, CommentRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        return Comment
                .builder()
                .id(requestDto.getId())
                .text(requestDto.getText())
                .item(itemById)
                .author(userById)
                .created(LocalDateTime.now().plusMinutes(1))
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto
                .builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
