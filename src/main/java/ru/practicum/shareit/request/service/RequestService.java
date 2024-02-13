package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.entity.Request;

import java.util.List;

/**
 * интерфейс для работы с данными о запросах на вещь ItemRequest
 */
public interface RequestService {

    /**
     * метод получения списка всех запросов, сделанных пользователем, вместе с данными об ответах на них.
     */
    List<Request> findAllRequestsByRequestor_id(Integer userId);

    /**
     * метод создания нового запроса
     */
    Request createRequest(Request request);

    /**
     * метод получения данных о запросе по его ID
     */
    Request findRequestById(Long requestId);

    /**
     * метод получения списка запросов, созданных другими пользователями (Pageable).
     */
    Page<Request> findAllRequestsPageAble(Integer userId, Integer from, Integer size);
}
