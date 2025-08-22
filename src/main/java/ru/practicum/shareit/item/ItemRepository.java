package ru.practicum.shareit.item;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.core.BaseRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Repository
public class ItemRepository extends BaseRepository<Item> {

    public <T> List<T> getAllItemOwner(Long userId, Function<Item, T> mapper) {
        return findAll()
                .stream()
                .filter(i -> Objects.equals(i.getOwnerId(), userId))
                .map(mapper)
                .toList();
    }

    public <T> List<T> findAvailable(String searchName, Function<Item, T> mapper) {
        return findAll()
                .stream()
                .filter(i -> i.getAvailable() && StringUtils.containsIgnoreCase(i.getName(), searchName))
                .map(mapper)
                .toList();

    }
}
