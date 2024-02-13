package ru.practicum.shareit.item.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс Item ("вещь") содержит описание вещи.
 */
@Entity
@Table(schema = "public", name = "items")
@Getter
@Setter
@Builder
@JsonPropertyOrder({"id", "name", "description", "available", "owner", "request"})
public class Item {

    /**
     * id — уникальный идентификатор вещи;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (nullable = false, updatable = false)
    private Long id;

    /**
     * name — краткое название;
     */
    @Column (nullable = false, length = 30)
    private String name;

    /**
     * description — развёрнутое описание;
     */
    @Column (nullable = false, length = 200)
    private String description;

    /**
     * available — статус о том, доступна или нет вещь для аренды;
     */
    @Column (nullable = false)
    private Boolean available;

    /**
     * owner — владелец вещи;
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    /**
     * request — если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка
     * на соответствующий запрос.
     */
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    public Item(Long id, String name, String description, Boolean available, User owner, Request request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

    @JsonCreator
    protected Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public Item() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId())
                && getName().equals(item.getName())
                && getDescription().equals(item.getDescription())
                && getAvailable().equals(item.getAvailable())
                && Objects.equals(getOwner(), item.getOwner())
                && Objects.equals(getRequest(), item.getRequest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getOwner(), getRequest());
    }
}
