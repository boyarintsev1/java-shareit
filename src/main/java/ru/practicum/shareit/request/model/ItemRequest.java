package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.entity.User;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Класс ItemRequest отвечает за запрос вещи.
 */

@Data
public class ItemRequest {

    /**
     * id — уникальный идентификатор запроса;
     */
    protected Integer id;

    /**
     * description — текст запроса, содержащий описание требуемой вещи;
     */
    @Size(max = 200, message = "Размер описания не может превышать 200 символов")
    protected String description;

    /**
     * requestor — пользователь, создавший запрос;
     */
    protected User requestor;

    /**
     * created — дата и время создания запроса.
     */
    @PastOrPresent(message = "Дата запроса не может быть из будущего")
    protected LocalDateTime created;

    public ItemRequest(Integer id, String description, User requestor, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }

}
