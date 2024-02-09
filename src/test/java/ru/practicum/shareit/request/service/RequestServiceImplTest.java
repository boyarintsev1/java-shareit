package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;
    @Captor
    private ArgumentCaptor<Request> requestArgumentCaptor;
    @InjectMocks
    RequestServiceImpl requestService;

    @Test
    void findAllRequestsPageAble_whenRequestIdIsValid_thenReturnRequest() {
        User requestor = new User(1, "Billy", "email55@yandex.com");
        Request request1 = Request.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(requestor)
                .created(LocalDateTime.now().minusMinutes(1))
                .build();
        Request request2 = Request.builder()
                .id(2L)
                .description("Нужна модель Солнечной системы в масштабе 1:1")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
        List<Request> expectedList = List.of(request1, request2);
        Mockito.when(requestRepository.findAllRequestsPageable(anyInt(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Request> actualList =
                requestService.findAllRequestsPageAble(requestor.getId(), 1, 10).getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllRequestsByRequestor_id__whenRequestorIdIsValid_thenReturnListSize2() {
        User requestor = new User(1, "Billy", "email55@yandex.com");
        Request request1 = Request.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(requestor)
                .created(LocalDateTime.now().minusMinutes(1))
                .build();
        Request request2 = Request.builder()
                .id(2L)
                .description("Нужна модель Солнечной системы в масштабе 1:1")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
        List<Request> expectedList = List.of(request1, request2);
        Mockito.when(requestRepository.findAllRequestsByRequestor_id(anyInt()))
                .thenReturn(expectedList);

        List<Request> actualList =
                requestService.findAllRequestsByRequestor_id(requestor.getId());

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findRequestById_whenRequestIdIsValid_thenReturnRequest() {
        User requestor = new User(1, "Billy", "email55@yandex.com");
        Request expectedRequest = Request.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(requestor)
                .created(LocalDateTime.now().minusMinutes(1))
                .build();
        Mockito.when(requestRepository.findById(expectedRequest.getId())).thenReturn(Optional.of(expectedRequest));

        Request actualRequest = requestService.findRequestById(expectedRequest.getId());

        assertEquals(expectedRequest, actualRequest, "Не равны");
    }

    @Test
    void createRequest_whenRequestIsValid_thenReturnRequest() {
        User requestor = new User(1, "Billy", "email55@yandex.com");
        Request expectedRequest = Request.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(requestor)
                .created(LocalDateTime.now().minusMinutes(1))
                .build();
        Mockito.when(requestRepository.save(expectedRequest)).thenReturn(expectedRequest);

        Request actualRequest = requestService.createRequest(expectedRequest);

        assertEquals(expectedRequest, actualRequest, "Не равны");
        verify(requestRepository).save(expectedRequest);
    }

}