package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.booking.enums.BookingStatus.WAITING;

@WebMvcTest
class ItemControllerMocMvcTest {
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

    private ItemDto createTestItemDto() {
        return new ItemDto(1L,
                "Дрель",
                "Простая дрель",
                true,
                1,
                null,
                null,
                null,
                List.of());
    }

    private Item createTestItem() {
        return Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .owner(createTestUser())
                .available(true)
                .request(null)
                .build();
    }

    private ItemRequestDto createTestItemRequestDto() {
        return ItemRequestDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
    }

    @SneakyThrows
    @Test
    void findAllItems_thenReturnListSizeOne() {
        User user = createTestUser();
        ItemDto itemDto = createTestItemDto();
        Item item = createTestItem();
        Mockito.when(itemService.findAllItems(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(item)));
        Mockito.when(itemMapper.toItemDtoForOwner(item)).thenReturn(itemDto);
        Mockito.when(userService.findUserById(anyInt())).thenReturn(user);

        String result = mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);

        verify(itemService, times(1)).findAllItems(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @SneakyThrows
    @Test
    void findItemById_thenReturnItemDto() {
        User user = createTestUser();
        ItemDto itemDto = createTestItemDto();
        Item item = createTestItem();
        Mockito.when(itemService.findItemById(anyLong())).thenReturn(item);
        Mockito.when(itemMapper.toItemDtoForOwner(item)).thenReturn(itemDto);

        String result = mockMvc.perform(
                        get("/items/{id}", item.getId())
                                .header("X-Sharer-User-Id", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(itemDto), result);

    }

    @SneakyThrows
    @Test
    void findItem_whenText_thenReturnItemDtoList() {
        ItemDto itemDto = createTestItemDto();
        Item item = createTestItem();
        Mockito.when(itemService.findItem(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(List.of(item)));
        Mockito.when(itemMapper.toItemDtoForBooker(item)).thenReturn(itemDto);

        String result = mockMvc.perform(
                        get("/items/search")
                                .param("text", "text"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);

        verify(itemService, times(1)).findItem(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @SneakyThrows
    @Test
    void createItem_whenRequestDtoIsCorrect_thenReturnUser() {
        User user = createTestUser();
        ItemDto itemDto = createTestItemDto();
        ItemRequestDto requestDto = createTestItemRequestDto();
        Mockito.when(itemMapper.toItemDtoForOwner(itemService.createItem(Mockito.any(), Mockito.any()))).thenReturn(itemDto);

        String result = mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", user.getId())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                // .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void createItem_whenRequestDtoIsNotValid_thenReturnedBadRequest() {
        ItemDto itemDto = createTestItemDto();
        ItemRequestDto requestDto = createTestItemRequestDto();
        requestDto.setName("");
        Mockito.when(itemMapper.toItemDtoForOwner(itemService.createItem(Mockito.any(), Mockito.any())))
                .thenReturn(itemDto);

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", "1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(itemMapper, never()).toItemDtoForOwner(itemService.createItem(Mockito.any(), Mockito.any()));
    }

    @SneakyThrows
    @Test
    void updateItem_whenDescriptionAndAvailable_thenReturnUser() {
        ItemDto itemDto = createTestItemDto();
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Аккумуляторная дрель + аккумулятор")
                .available(false)
                .build();
        Mockito.when(itemMapper.toItemDto(itemService.updateItem(Mockito.any(), Mockito.any(), Mockito.any())))
                .thenReturn(itemDto);
        String result = mockMvc.perform(
                        patch("/items/{id}", itemDto.getId())
                                .header("X-Sharer-User-Id", "1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }


    @SneakyThrows
    @Test
    void deleteItem_whenDeleteItem_thenStatus200() {
        Long itemId = 1L;

        mockMvc.perform(
                        delete("/items/{id}", itemId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(Mockito.any());
    }

    @SneakyThrows
    @Test
    void createComment_whenCommentDtoIsCorrect_thenStatus200() {
        User user = createTestUser();
        Item item = createTestItem();
        CommentDto requestDto = CommentDto.builder()
                .text("Add comment from user1")
                .build();
        Comment comment = new Comment(1L, "Add comment from user1", item, user, LocalDateTime.now());
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusHours(5))
                .end(LocalDateTime.now().plusDays(1L))
                .item(item)
                .booker(user)
                .status(WAITING)
                .build();
        CommentDto commentDto = new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
        Mockito.when(userService.findUserById(anyInt())).thenReturn(user);
        Mockito.when(itemService.checkUserBookedItemInPast(anyLong(), anyInt())).thenReturn(List.of(booking));
        Mockito.when(commentMapper.toComment(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(comment);
        Mockito.when(commentMapper.toCommentDto(itemService.createComment(Mockito.any()))).thenReturn(commentDto);

        String result = mockMvc.perform(
                        post("/items/{itemId}/comment", item.getId())
                                .header("X-Sharer-User-Id", user.getId())
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}