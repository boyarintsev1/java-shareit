package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;

import java.util.List;
import java.util.Objects;

/**
 * Класс ItemDto ("вещь") содержит описание вещи Item, которое будет возвращено пользователю.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Integer owner;
    private Long requestId;
    private BookingDtoForOwner lastBooking;
    private BookingDtoForOwner nextBooking;
    private List<CommentDto> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDto)) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(getId(), itemDto.getId()) && getName().equals(itemDto.getName())
                && getDescription().equals(itemDto.getDescription()) && getAvailable().equals(itemDto.getAvailable())
                && Objects.equals(getOwner(), itemDto.getOwner())
                && Objects.equals(getRequestId(), itemDto.getRequestId())
                && Objects.equals(getLastBooking(), itemDto.getLastBooking())
                && Objects.equals(getNextBooking(), itemDto.getNextBooking())
                && Objects.equals(getComments(), itemDto.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getOwner(),
                getRequestId(), getLastBooking(), getNextBooking(), getComments());
    }

}
