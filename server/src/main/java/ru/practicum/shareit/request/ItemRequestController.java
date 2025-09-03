package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody ItemRequestDto itemRequestDto) throws NotFoundException {
        return service.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) throws NotFoundException {
        return service.getAllItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequestsOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId) throws NotFoundException {
        return service.getAllItemRequestsOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getEntity(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) throws NotFoundException {
        return service.getEntity(requestId);
    }

}
