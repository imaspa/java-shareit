package ru.practicum.shareit.item;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.comment.dto.CommentsInputDto;
import ru.practicum.shareit.core.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
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

    public ResponseEntity<Object> find(Long userId, String text) {
        String path = "/search?text=" + text;
        return get(path, userId);
    }

    public ResponseEntity<Object> getAllItemOwner(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getEntity(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> createComments(Long userId, Long itemId, CommentsInputDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }


}