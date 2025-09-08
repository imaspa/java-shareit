package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.comment.dto.CommentsInputDto;
import ru.practicum.shareit.core.ClientBase;
import ru.practicum.shareit.item.dto.ItemDto;


public class ItemClient extends ClientBase {
    private static final String API_PREFIX = "/items";

    public ItemClient(RestTemplate rest) {
        super(rest);
    }


    public ResponseEntity<Object> find(Long userId, String text) {
        String path = "/search?text=" + text;
        return get(path, userId);
    }

    public ResponseEntity<Object> getAllItemOwner(Long userId) {
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> getEntity(Long userId, Long itemId) {
        return get(API_PREFIX + "/" + itemId, userId);
    }

    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto itemDto) {
        return patch(API_PREFIX + "/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> createComments(Long userId, Long itemId, CommentsInputDto commentDto) {
        return post(API_PREFIX + "/" + itemId + "/comment", userId, commentDto);
    }


}