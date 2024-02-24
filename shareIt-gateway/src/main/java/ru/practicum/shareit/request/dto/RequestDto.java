package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Класс RequestDto содержит описание запроса Request, которое будет возвращено пользователю.
 */
@Getter
@Builder
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestDto)) return false;
        RequestDto that = (RequestDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getCreated(), that.getCreated()) && Objects.equals(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getCreated(), getItems());
    }
}

