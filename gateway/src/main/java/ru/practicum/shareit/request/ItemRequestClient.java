package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.core.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> {
                            CloseableHttpClient httpClient = HttpClients.createDefault();
                            return new HttpComponentsClientHttpRequestFactory(httpClient);
                        })
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, @Valid ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getAllItemRequests(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllItemRequestsOtherUsers(Long userId) {
        return get("/all", userId);
    }

    public ResponseEntity<Object> getEntity(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }

}