package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.*;

/**
 * Класс UserRequestDto содержит информацию c входящего POST/PATCH запроса о пользователях (user).
 */

@Getter
@Setter
@JsonPropertyOrder({"id", "name", "email"})
@AllArgsConstructor
public class UserRequestDto {

        /**
         * id — уникальный идентификатор пользователя;
         */
        @EqualsAndHashCode.Exclude
        private Integer id;

        /**
         * name — имя или логин пользователя;
         */
        @NotNull(message = "Значение поля Name не может быть пустым")
        @NotEmpty
        @NotBlank
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
}
