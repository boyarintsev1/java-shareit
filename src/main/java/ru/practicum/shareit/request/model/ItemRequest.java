package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Класс ItemRequest отвечает за запрос вещи. Его атрибуты:
 * id — уникальный идентификатор запроса;
 * description — текст запроса, содержащий описание требуемой вещи;
 * requestor — пользователь, создавший запрос;
 * created — дата и время создания запроса.
 */

@Data
public class ItemRequest {

    protected Integer id;

    @Size(max = 200, message = "Размер описания не может превышать 200 символов")
    protected String description;
    protected User requestor;

    @PastOrPresent(message = "Дата запроса не может быть из будущего")
    protected LocalDateTime created;

    public ItemRequest(Integer id, String description, User requestor, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }

}
