package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.IncomingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Класс-контроллер по запросам на вещь Request, ответственный за валидацию входящих данных
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestClient requestClient;

    /**
     * метод валидации входящих данных перед получением списка всех запросов, сделанных пользователем,
     * вместе с данными об ответах на них.
     */
    @GetMapping
    public ResponseEntity<Object>  findAllRequestsByUserId(
            @Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return requestClient.findAllRequestsByUserId(userId);
    }

    /**
     * метод валидации входящих данных перед получением данных о запросе по его ID
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @Positive @NotNull @PathVariable("requestId") Long requestId) {
        return requestClient.findRequestById(userId, requestId);
    }

    /**
     * метод валидации входящих данных перед получением списка запросов, созданных другими пользователями (Pageable).
     */
    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequestsPageAble(
            @Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PositiveOrZero @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", required = false, defaultValue = "1") Integer size) {
        return requestClient.findAllRequestsPageAble(userId, from, size);

    }

    /**
     * метод валидации входящих данных перед созданием нового запроса
     */
    @PostMapping
    public ResponseEntity<Object> createRequest(@Positive @NotNull @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @Valid @RequestBody IncomingRequestDto incomingRequestDto) {
        return requestClient.createRequest(userId, incomingRequestDto);
    }
}

