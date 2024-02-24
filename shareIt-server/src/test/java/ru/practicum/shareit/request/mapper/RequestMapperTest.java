package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {
    @Mock
    ItemService itemService;
    @Mock
    UserService userService;
    @Mock
    ItemMapper itemMapper;
    @InjectMocks
    RequestMapper requestMapper;

    User requestor = new User(1, "Billy", "email55@yandex.com");
    IncomingRequestDto incomingRequestDto = IncomingRequestDto
            .builder()
            .description("Хотел бы воспользоваться щёткой для обуви")
            .build();
    Request request = Request.builder()
            .id(1L)
            .description("Хотел бы воспользоваться щёткой для обуви")
            .requestor(requestor)
            .created(LocalDateTime.now().minusMinutes(1))
            .build();
    RequestDto requestDto = new RequestDto(
            1L, "Хотел бы воспользоваться щёткой для обуви", request.getCreated(), List.of());

    @Test
    void toRequest_whenIncomingRequestDtoIsValid_thenReturnRequest() {
        Request expectedRequest = Request.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(requestor)
                .build();
        Mockito.when(userService.findUserById(anyInt())).thenReturn(requestor);

        Request actualRequest = requestMapper.toRequest(requestor.getId(), incomingRequestDto);
        expectedRequest.setCreated(actualRequest.getCreated());

        assertEquals(expectedRequest, actualRequest, "Не равны");
    }

    @Test
    void toRequest_whenIncomingRequestDtoIsNull_thenReturnNull() {
        Request actualRequest = requestMapper.toRequest(requestor.getId(), null);

        assertNull(actualRequest, "Не равны");
    }

    @Test
    void toRequestDto_whenRequestIsValid_thenReturnRequestDto() {
        Mockito.when(itemService.findAllItemsByOwnerAndRequest(anyInt())).thenReturn(List.of());

        RequestDto actualRequestDto = requestMapper.toRequestDto(request);

        assertEquals(requestDto, actualRequestDto, "Не равны");
    }

    public RequestDto toRequestDto(Request request) {
        if (request == null) {
            return null;
        }
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                itemService.findAllItemsByOwnerAndRequest(request.getRequestor().getId())
                        .stream()
                        .map(itemMapper::toItemDtoForRequest)
                        .collect(Collectors.toList()));
    }


    @Test
    void toRequestDto_whenRequestIsNull_thenReturnNull() {
        RequestDto actualRequestDto = requestMapper.toRequestDto(null);

        assertNull(actualRequestDto, "Не равны");
    }
}