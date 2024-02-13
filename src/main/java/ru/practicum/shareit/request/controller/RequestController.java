package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.IncomingRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс-контроллер по запросам на вещь ItemRequest
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    public final UserService userService;
    public final RequestService requestService;
    public final RequestMapper requestMapper;

    /**
     * метод получения списка всех запросов, сделанных пользователем, вместе с данными об ответах на них.
     */
    @GetMapping
    public List<RequestDto> findAllRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        if (userService.findUserById(userId) == null)
            throw new IncorrectIdException("UserID");
        return requestService.findAllRequestsByRequestor_id(userId)
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * метод получения данных о запросе по его ID
     */
    @GetMapping("/{requestId}")
    public RequestDto findRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @PathVariable("requestId") Long requestId) {
        if (userService.findUserById(userId) == null)
            throw new IncorrectIdException("UserID");
        return requestMapper.toRequestDto(requestService.findRequestById(requestId));
    }

    /**
     * метод получения списка запросов, созданных другими пользователями (Pageable).
     */
    @GetMapping("/all")
    public List<RequestDto> findAllRequestsPageAble(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "1") Integer size) {
        if ((from < 0) || (size <= 0))
            throw new ValidationException("неверно указаны параметры запросы from (д.б.>=0) или size (д.б.>0",
                    HttpStatus.BAD_REQUEST);
        if (userService.findUserById(userId) == null)
            throw new IncorrectIdException("UserID");
        return (requestService.findAllRequestsPageAble(userId, from, size).map(requestMapper::toRequestDto).getContent());

    }

    /**
     * метод создания нового запроса
     */
    @PostMapping
    public RequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @Valid @RequestBody IncomingRequestDto requestDto) {
        Request request = requestMapper.toRequest(userId, requestDto);
        return requestMapper.toRequestDto(requestService.createRequest(request));
    }
}

