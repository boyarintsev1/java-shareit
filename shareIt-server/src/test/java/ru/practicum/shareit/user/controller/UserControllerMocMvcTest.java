package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class UserControllerMocMvcTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper mapper;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private BookingMapper bookingMapper;
    @MockBean
    private RequestService requestService;
    @MockBean
    private RequestMapper requestMapper;

    private UserRequestDto createTestUserRequestDto() {
        return UserRequestDto.builder()
                .name("Billy")
                .email("email55@yandex.com")
                .build();
    }

    private User createTestUser() {
        return new User(1, "Billy", "email55@yandex.com");
    }

    private UserDto createTestUserDto() {
        return UserDto.builder()
                .id(1)
                .name("Billy")
                .email("email55@yandex.com")
                .build();
    }

    @SneakyThrows
    @Test
    void findAllUsers_whenUserIdIsValid_thenReturnListLength1AndStatus200() {
        User user = createTestUser();
        UserDto userDto = createTestUserDto();
        Mockito.when(userService.findAllUsers()).thenReturn(List.of(user));
        Mockito.when(mapper.toUserDto(user)).thenReturn(userDto);

        String result = mockMvc.perform(
                        get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(userDto))))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(List.of(userDto)), result);
    }

    @SneakyThrows
    @Test
    void findUserById_whenUserIdIsValid_thenReturnUserAndStatus200() {
        User user = createTestUser();
        UserDto userDto = createTestUserDto();
        Mockito.when(mapper.toUserDto(userService.findUserById(anyInt()))).thenReturn(userDto);

        String result = mockMvc.perform(
                        get("/users/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void createUser_whenUserRequestDtoIsValid_thenReturnUserAndStatus200() {
        UserRequestDto requestDto = createTestUserRequestDto();
        UserDto userDto = createTestUserDto();
        Mockito.when(mapper.toUserDto(userService.createUser(Mockito.any()))).thenReturn(userDto);

        String result = mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserRequestDtoIsValid_thenReturnUserAndStatus200() {
        UserDto userDto = createTestUserDto();
        userDto.setEmail("user@user.com");
        User user = createTestUser();
        UserRequestDto requestDto = UserRequestDto.builder()
                .email("user@user.com")
                .build();
        Mockito.when((mapper.toUserDto(userService.updateUser(Mockito.any(), Mockito.any()))))
                .thenReturn(userDto);

        String result = mockMvc.perform(
                        patch("/users/{id}", user.getId())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void deleteUser_whenUserIdIsCorrect_thenReturnStatus200() {
        Integer userId = 1;

        mockMvc.perform(
                        delete("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(Mockito.any());
    }

}