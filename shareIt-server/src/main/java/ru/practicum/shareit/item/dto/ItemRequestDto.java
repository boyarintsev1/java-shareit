package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import ru.practicum.shareit.user.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Класс ItemRequestDto содержит информацию c входящего POST/PATCH запроса о вещах (item).
 */
@Getter
@Setter
@JsonPropertyOrder({"id", "name", "description", "available", "owner", "request"})
@Builder
@AllArgsConstructor
public class ItemRequestDto {

    /**
     * id — уникальный идентификатор вещи;
     */
    @EqualsAndHashCode.Exclude
    private Long id;

    /**
     * name — краткое название;
     */
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 30, message = "Название вещи должно быть длиной не более 30 символов")
    private String name;

    /**
     * description — развёрнутое описание;
     */
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 200, message = "Размер описания не может превышать 200 символов")
    private String description;

    /**
     * available — статус о том, доступна или нет вещь для аренды;
     */
    @EqualsAndHashCode.Exclude
    @NotNull
    private Boolean available;

    /**
     * owner — владелец вещи;
     */
    @EqualsAndHashCode.Exclude
    private User owner;

    /**
     * request — если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка
     * на соответствующий запрос.
     */
    @EqualsAndHashCode.Exclude
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRequestDto)) return false;
        ItemRequestDto that = (ItemRequestDto) o;
        return Objects.equals(getId(), that.getId()) && getName().equals(that.getName()) && getDescription().equals(that.getDescription()) && getAvailable().equals(that.getAvailable()) && Objects.equals(getOwner(), that.getOwner()) && Objects.equals(getRequestId(), that.getRequestId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getOwner(), getRequestId());
    }

}
