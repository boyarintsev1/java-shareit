package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class RequestControllerMocMvcTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
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

    private User createTestUser() {
        User user = new User();
        user.setId(1);
        return user;
    }

    private IncomingRequestDto createTestIncomingRequestDto() {
        return IncomingRequestDto.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .build();
    }

    private RequestDto createTestRequestDto() {
        return RequestDto.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(LocalDateTime.now())
                .items(List.of())
                .build();
    }

    private Request createTestRequest() {
        return Request.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(createTestUser())
                .created(LocalDateTime.now())
                .build();
    }

    @SneakyThrows
    @Test
    void findAllRequestsByUserId_whenUserIdIsValid_thenReturnListLength1AndStatus200() {
        User user = createTestUser();
        RequestDto requestDto = createTestRequestDto();
        Request request = createTestRequest();
        Mockito.when(userService.findUserById(Mockito.any())).thenReturn(user);
        Mockito.when(requestMapper.toRequestDto(Mockito.any())).thenReturn(requestDto);
        Mockito.when(requestService.findAllRequestsByRequestor_id(anyInt()))
                .thenReturn(List.of(request));

        String result = mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(requestDto))))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @SneakyThrows
    @Test
    void findRequestById_whenRequestIdIsValid_thenReturnRequestAndStatus200() {
        User user = createTestUser();
        RequestDto requestDto = createTestRequestDto();
        Request request = createTestRequest();
        Mockito.when(userService.findUserById(Mockito.any())).thenReturn(user);
        Mockito.when(requestMapper.toRequestDto(requestService.findRequestById(anyLong()))).thenReturn(requestDto);

        String result = mockMvc.perform(
                        get("/requests/{requestId}", request.getId())
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }

    @SneakyThrows
    @Test
    void findAllRequestsPageAble_whenUserIdIsValid_thenReturnListLength1AndStatus200() {
        User user = createTestUser();
        RequestDto requestDto = createTestRequestDto();
        Request request = createTestRequest();

        Mockito.when(userService.findUserById(Mockito.any())).thenReturn(user);
        Mockito.when(requestMapper.toRequestDto(Mockito.any())).thenReturn(requestDto);
        Mockito.when(requestService.findAllRequestsPageAble(anyInt(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(request)));

        String result = mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(requestDto))))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @SneakyThrows
    @Test
    void findAllRequestsPageAble_whenUserIsNotFound_thenExceptionThrows() {
        User user = createTestUser();
        RequestDto requestDto = createTestRequestDto();
        Request request = createTestRequest();
        Mockito.when(userService.findUserById(Mockito.any())).thenReturn(null);
        Mockito.when(requestMapper.toRequestDto(Mockito.any())).thenReturn(requestDto);
        Mockito.when(requestService.findAllRequestsPageAble(anyInt(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(request)));

        mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(
                        IncorrectIdException.class, Objects.requireNonNull(result.getResolvedException()).getClass()));
    }

    @SneakyThrows
    @Test
    void findAllRequestsPageAble_whenFromIsNotValid_thenExceptionThrows() {
        User user = createTestUser();
        RequestDto requestDto = createTestRequestDto();
        Request request = createTestRequest();
        Mockito.when(userService.findUserById(Mockito.any())).thenReturn(user);
        Mockito.when(requestMapper.toRequestDto(Mockito.any())).thenReturn(requestDto);
        Mockito.when(requestService.findAllRequestsPageAble(anyInt(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(request)));

        mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", user.getId())
                                .param("from", "-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(
                        ValidationException.class, Objects.requireNonNull(result.getResolvedException()).getClass()));
    }

    @SneakyThrows
    @Test
    void findAllRequestsPageAble_whenSizeIsNotValid_thenExceptionThrows() {
        User user = createTestUser();
        RequestDto requestDto = createTestRequestDto();
        Request request = createTestRequest();
        Mockito.when(userService.findUserById(Mockito.any())).thenReturn(user);
        Mockito.when(requestMapper.toRequestDto(Mockito.any())).thenReturn(requestDto);
        Mockito.when(requestService.findAllRequestsPageAble(anyInt(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(request)));

        mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", user.getId())
                                .param("size", "0"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(
                        ValidationException.class, Objects.requireNonNull(result.getResolvedException()).getClass()));
    }

    @SneakyThrows
    @Test
    void createRequest_whenIncomingRequestDtoIsValid_thenReturnRequestDtoAndStatus200() {
        User user = createTestUser();
        IncomingRequestDto incomingRequestDto = createTestIncomingRequestDto();
        RequestDto requestDto = createTestRequestDto();
        Mockito.when(requestMapper.toRequestDto(requestService.createRequest(Mockito.any()))).thenReturn(requestDto);

        String result = mockMvc.perform(
                        post("/requests")
                                .header("X-Sharer-User-Id", user.getId())
                                .content(objectMapper.writeValueAsString(incomingRequestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }
}