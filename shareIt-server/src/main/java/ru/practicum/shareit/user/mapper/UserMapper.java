package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserShort;
import ru.practicum.shareit.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequestDto requestDto);

    UserDto toUserDto(User user);

    default UserShort toUserShort(User user) {
        if (user == null) {
            return null;
        }
        return UserShort
                .builder()
                .id(user.getId())
                .build();
    }
}
