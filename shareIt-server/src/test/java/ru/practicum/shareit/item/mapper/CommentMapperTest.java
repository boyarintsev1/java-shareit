package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {
    @InjectMocks
    CommentMapper commentMapper;

    @Test
    void toComment_whenRequestDtoIsNotNull_thenReturnComment() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User author = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        CommentRequestDto requestDto = CommentRequestDto.builder()
                .text("Add comment from author")
                .build();
        Comment expectedComment = new Comment(
                null, "Add comment from author", item, author, LocalDateTime.now());

        Comment actualComment = commentMapper.toComment(author, item, requestDto);
        expectedComment.setCreated(actualComment.getCreated());
        assertEquals(expectedComment, actualComment, "Не равны");
    }

    @Test
    void toComment_whenRequestDtoIsNull_thenReturnComment() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User author = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);

        Comment actualComment = commentMapper.toComment(author, item, null);

        assertNull(actualComment, "Не равны");
    }

    @Test
    void toCommentDto_whenCommentIsNotNull_thenReturnCommentDto() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User author = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Comment comment = new Comment(
                1L, "Add comment from author", item, author, LocalDateTime.now().plusMinutes(1));
        CommentDto expectedCommentDto = new CommentDto(
                1L, "Add comment from author", author.getName(), comment.getCreated());

        CommentDto actualCommentDto = commentMapper.toCommentDto(comment);

        assertEquals(expectedCommentDto, actualCommentDto, "Не равны");
    }

    @Test
    void toCommentDto_whenCommentIsNull_thenReturnNull() {
        CommentDto actualCommentDto = commentMapper.toCommentDto(null);

        assertNull(actualCommentDto, "Не равны");
    }
}
