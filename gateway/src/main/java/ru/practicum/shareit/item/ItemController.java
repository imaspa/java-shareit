package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.comment.dto.CommentsInputDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping("/search")
    public ResponseEntity<Object> find(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam("text") String text) {
        return itemClient.find(userId, text);
    }


    @GetMapping()
    public ResponseEntity<Object> getAllItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllItemOwner(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getEntity(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemClient.getEntity(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") @NotNull(message = "параметр: `Владелец` обязателен к заполнению") Long userId,
            @RequestBody @Valid ItemDto itemDto) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody @Valid ItemDto itemDto) {
        return itemClient.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId,
                                                @RequestBody @Valid CommentsInputDto commentsInputDto) {
        return itemClient.createComments(userId, itemId, commentsInputDto);
    }
}
