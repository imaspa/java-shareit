package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.core.ClientBase;
import ru.practicum.shareit.user.dto.UserDto;


public class UserClient extends ClientBase {
    private static final String API_PREFIX = "/users";

    public UserClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getEntity(Long userId) {
        return get(API_PREFIX + "/" + userId);
    }

    public ResponseEntity<Object> create(@Valid UserDto userDto) {
        return post(API_PREFIX, userDto);
    }

    public ResponseEntity<Object> update(Long userId, @Valid UserDto userDto) {
        return patch(API_PREFIX + "/" + userId, userDto);
    }

    public ResponseEntity<Object> delete(Long userId) {
        return delete(API_PREFIX + "/" + userId);
    }
}