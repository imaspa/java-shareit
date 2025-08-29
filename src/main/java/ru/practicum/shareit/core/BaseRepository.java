package ru.practicum.shareit.core;

import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.core.model.Identifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRepository<T extends Identifiable> {
    private final Map<Long, T> storage = new HashMap<>();

    public T createOrUpdate(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public T findById(Long id) throws NotFoundException {
        if ((id == null) || (!existsById(id))) {
            throw new NotFoundException("Запись не найдена");
        }
        return storage.get(id);
    }

    public Boolean checkExist(Long id) {
        return id != null && storage.containsKey(id);
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public Long getNextId() {
        long currentMaxId = storage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}