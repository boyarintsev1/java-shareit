package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.request.dto.IncomingRequestDto;

import java.util.Map;

/**
 * Класс-клиент по запросам Request
 */
@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> findAllRequestsByUserId(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findRequestById(Integer userId, Long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> findAllRequestsPageAble(Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", Long.valueOf(userId), parameters);
    }

    public ResponseEntity<Object> createRequest(Integer userId, IncomingRequestDto incomingRequestDto) {
        return post("", userId, incomingRequestDto);
    }
}
