package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
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

    public ResponseEntity<Object> getEntity(Long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> create(@Valid UserDto userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> update(Long userId, @Valid UserDto userDto) {
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> delete(Long userId) {
        return delete("/" + userId);
    }
}