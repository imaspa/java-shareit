package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDto create(@Valid UserDto userDto) throws ConditionsException, ConflictException {
        if (userDto.getId() != null) {
            throw new ConditionsException("При создании записи запрещена передача идентификатора");
        }
        if (isEmailExistsAnotherUser(userDto)) {
            throw new ConflictException("Адрес электронной почты уже используется");
        }
        var entity = repository.createOrUpdate(mapper.toEntity(userDto, repository.getNextId()));
        return mapper.toDto(entity);
    }

    public UserDto update(Long userId, UserDto userDto) throws NotFoundException, ConflictException {
        var newUserDto = mapper.updateDto(getEntity(userId), userDto);
        if (isEmailExistsAnotherUser(newUserDto)) {
            throw new ConflictException("Адрес электронной почты уже используется");
        }
        var entity = repository.createOrUpdate(mapper.toEntity(newUserDto, userId));
        return mapper.toDto(entity);
    }

    public List<UserDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public Boolean isEmailExistsAnotherUser(UserDto userDto) {
        return repository.findAll()
                .stream()
                .filter(u -> !Objects.equals(u.getId(), userDto.getId()))
                .anyMatch(u -> u.getEmail().equals(userDto.getEmail()));
    }

    public UserDto getEntity(Long userId) throws NotFoundException {
        return mapper.toDto(repository.findById(userId));
    }

    public Boolean userIsExist(Long userId) {
        return repository.existsById(userId);
    }

    public void delete(Long userId) {
        repository.deleteById(userId);
    }
}
