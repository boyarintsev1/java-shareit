package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * Класс ItemRequestDto содержит информацию c входящего POST/PATCH запроса о заказах (request).
 */

@Getter
@Setter
@JsonPropertyOrder({"id", "description", "requestor", "created"})
@Builder
@AllArgsConstructor
public class IncomingRequestDto {

    /**
     * id — уникальный идентификатор запроса;
     */
    @Null (message = "Нельзя задавать ID")
    private Long id;

    /**
     * description — текст запроса, содержащий описание требуемой вещи;
     */
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 500, message = "Размер описания должен быть в пределах от 1 до 500 символов")
    private String description;

    /**
     * requestor — пользователь, создавший запрос;
     */
    private Integer requestorId;

    /**
     * created — дата и время создания запроса.
     */
    @FutureOrPresent(message = "Дата запроса не может быть из будущего")
    private LocalDateTime created;
}

