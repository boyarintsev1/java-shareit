package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {

    @InjectMocks
    private UserMapperImpl userMapper;

    UserRequestDto requestDto = UserRequestDto
            .builder()
            .name("Billy")
            .email("email55@yandex.com")
            .build();
    User user = new User(1, "Billy", "email55@yandex.com");
    UserDto userDto = new UserDto(1, "Billy", "email55@yandex.com");

    @Test
    void toUser_whenRequestDtoIsCorrect_thenReturnUser() {
        User actualUser = userMapper.toUser(requestDto);

        assertEquals(user, actualUser, "Не равны");
    }

    @Test
    void toUser_whenRequestDtoIsNull_thenReturnNull() {
        User actualUser = userMapper.toUser(null);

        assertNull(actualUser, "Не равны");
    }

    @Test
    void toUserDto_whenUserIsCorrect_thenReturnUserDto() {
        UserDto actualUserDto = userMapper.toUserDto(user);

        assertEquals(userDto, actualUserDto, "Не равны");
    }

    @Test
    void toUserDto_whenUserIsNull_thenReturnNull() {
        UserDto actualUserDto = userMapper.toUserDto(null);

        assertNull(actualUserDto, "Не равны");
    }
}

