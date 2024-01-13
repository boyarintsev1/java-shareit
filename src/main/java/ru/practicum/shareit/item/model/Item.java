package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Класс Item ("вещь") содержит описание вещи.
 */
@Data
@Getter
@JsonPropertyOrder({"id", "name", "description", "available", "owner", "request"})
public class Item {

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
    @Size(min = 1, max = 30, message
            = "Название вещи должно быть длиной не более 30 символов")
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
    protected ItemRequest request;

    protected Item(Long id, String name, String description, Boolean available, User owner, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

    protected Item(Long id, String name, String description, Boolean available, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    @JsonCreator
    protected Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    protected Item() {
    }

}
