package ru.practicum.shareit.item.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс Comment ("отзыв") содержит отзывы пользователей, бравших вещь в аренду.
 */
@Entity
@Table(schema = "public", name = "comments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "text", "item_id", "author_id", "created"})
public class Comment {

    /**
     * id — уникальный идентификатор отзыва;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    /**
     * text — содержимое комментария;
     */
    @Column(nullable = false, length = 1000)
    private String text;

    /**
     * item — вещь, к которой относится комментарий;
     */
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    /**
     * author — автор комментария;
     */
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /**
     * created — дата создания комментария;
     */
    @Column(nullable = false)
    private LocalDateTime created;
}

