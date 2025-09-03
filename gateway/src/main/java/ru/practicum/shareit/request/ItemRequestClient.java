package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.core.ClientBase;
import ru.practicum.shareit.request.dto.ItemRequestDto;


public class ItemRequestClient extends ClientBase {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(RestTemplate rest) {
        super(rest);
    }


    public ResponseEntity<Object> create(Long userId, @Valid ItemRequestDto itemRequestDto) {
        return post(API_PREFIX, userId, itemRequestDto);
    }

    public ResponseEntity<Object> getAllItemRequests(Long userId) {
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> getAllItemRequestsOtherUsers(Long userId) {
        return get(API_PREFIX + "/all", userId);
    }

    public ResponseEntity<Object> getEntity(Long userId, Long requestId) {
        return get(API_PREFIX + "/" + requestId, userId);
    }

}