package ru.practicum.shareit.request.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Класс ItemRequest отвечает за запрос вещи.
 */
@Entity
@Table(schema = "public", name = "requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    /**
     * id — уникальный идентификатор запроса;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (nullable = false, updatable = false)
    private Long id;

    /**
     * description — текст запроса, содержащий описание требуемой вещи;
     */
    @Column(nullable = false)
    @Size(min = 1, max = 500, message = "Размер описания должен быть в пределах от 1 до 500 символов")
    private String description;

    /**
     * requestor — пользователь, создавший запрос;
     */
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    /**
     * created — дата и время создания запроса.
     */
    @Column(nullable = false)
    @PastOrPresent(message = "Дата запроса не может быть из будущего")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}

