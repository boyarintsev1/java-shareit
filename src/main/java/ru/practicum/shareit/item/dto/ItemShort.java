package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Класс ItemShort содержит краткое описание вещи Item, которое будет возвращено пользователю.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemShort {
    private Long id;
    private String name;
}
