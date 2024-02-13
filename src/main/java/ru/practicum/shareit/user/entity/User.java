package ru.practicum.shareit.user.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс User содержит информацию о пользователях (user).
 */
@Entity
@Table(schema = "public", name = "users", uniqueConstraints =
        @UniqueConstraint(name = "UQ_USER_EMAIL", columnNames = {"email"}))
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonPropertyOrder({"id", "name", "email"})
public class User {

    /**
     * id — уникальный идентификатор пользователя;
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false, updatable = false)
    private Integer id;

    /**
     * name — имя или логин пользователя;
     */
    @Column (name = "name", nullable = false, length = 30)
    private String name;

    /**
     * email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
     */
    @Column (name = "email", nullable = false)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail());
    }
}

