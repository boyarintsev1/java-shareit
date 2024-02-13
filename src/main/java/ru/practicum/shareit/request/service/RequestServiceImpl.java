package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.request.repository.RequestRepository;

import java.util.List;

/**
 * Класс RequestServiceImpl содержит имплементацию интерфейса о запросах (request).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public Page<Request> findAllRequestsPageAble(Integer userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        Page<Request> requestPage = requestRepository.findAllRequestsPageable(userId, page);
        return requestPage;
    }

    @Transactional
    @Override
    public List<Request> findAllRequestsByRequestor_id(Integer userId) {
        return requestRepository.findAllRequestsByRequestor_id(userId);
    }

    @Transactional
    @Override
    public Request findRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new IncorrectIdException("RequestID"));
    }

    @Transactional
    @Override
    public Request createRequest(Request request) {
        log.info("Создан новый запрос на вещь.");
        return requestRepository.save(request);
    }
}
