package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/search")
    public List<ItemDto> find(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam("text") String text) {
        return itemService.find(text);
    }


    @GetMapping()
    public List<ItemDto> getAllItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemOwner(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getEntity(@PathVariable Long itemId) throws NotFoundException {
        return itemService.getEntity(itemId);
    }

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemDto itemDto) throws ConditionsException, NotFoundException {
        var itemDtoWithOwner = itemDto.toBuilder().ownerId(userId).build();
        return itemService.create(itemDtoWithOwner);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto) throws NotFoundException {
        var itemDtoWithOwner = itemDto.toBuilder().ownerId(userId).build();
        return itemService.update(itemId, itemDtoWithOwner);
    }


}
