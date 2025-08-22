package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;
    private final UserService userService;

    public ItemDto create(@Valid ItemDto itemDto) throws ConditionsException, NotFoundException {
        if (itemDto.getId() != null) {
            throw new ConditionsException("При создании записи запрещена передача идентификатора");
        }
        if (!userService.userIsExist(itemDto.getOwnerId())) {
            throw new NotFoundException("Собственник не найден");
        }
        var entity = repository.createOrUpdate(mapper.toEntity(itemDto, repository.getNextId()));
        return mapper.toDto(entity);

    }

    public ItemDto update(Long itemId, ItemDto itemDto) throws NotFoundException {
        var itemBd = getEntity(itemId);
        if (isItemAnotherUser(itemBd, itemDto)) {
            throw new NotFoundException("Редактировать возможно только свои записи");
        }

        var newItemDto = mapper.updateDto(itemBd, itemDto);
        var entity = repository.createOrUpdate(mapper.toEntity(newItemDto, itemId));
        return mapper.toDto(entity);
    }

    public List<ItemDto> getAllItemOwner(Long userId) {
        return repository.findAll()
                .stream()
                .filter(i -> Objects.equals(i.getOwnerId(), userId))
                .map(mapper::toDto)
                .toList();
    }

    public ItemDto getEntity(Long id) throws NotFoundException {
        return mapper.toDto(repository.findById(id));
    }

    public Boolean isItemAnotherUser(ItemDto item1, ItemDto item2) {
        if (item1 == null || item2 == null) return true;
        return !Objects.equals(item1.getOwnerId(), item2.getOwnerId());
    }


    public List<ItemDto> find(String searchName) {
        if (searchName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return repository.findAll()
                .stream()
                .filter(i -> i.getAvailable() && StringUtils.containsIgnoreCase(i.getName(), searchName))
                .map(mapper::toDto)
                .toList();

    }

}
