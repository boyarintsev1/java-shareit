package ru.practicum.shareit.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * Класс User содержит информацию о пользователях (user).
 */

@Data
@JsonPropertyOrder({"id", "name", "email"})
public class User {

    /**
     * id — уникальный идентификатор пользователя;
     */
    @EqualsAndHashCode.Exclude
    private Integer id;

    /**
     * name — имя или логин пользователя;
     */
    @NotNull(message = "Значение поля Name не может быть пустым")
    @NotEmpty(message = "Значение поля Name не может быть пустым")
    @NotBlank(message = "Значение поля Name не может быть пустым")
    @EqualsAndHashCode.Exclude
    @Size(min = 1, max = 30, message
            = "Имя пользователя должно быть длиной от 1 до 30 символов")
    private String name;

    /**
     * email — адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).
     */
    @NotNull(message = "Значение поля Email не может быть пустым")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;

    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @JsonCreator
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

