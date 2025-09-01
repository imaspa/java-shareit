package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.Users;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public UserDto create(@Valid UserDto userDto) throws ConditionsException, ConflictException {
        log.info("Создать УЗП (старт). email: {}", userDto.getEmail());
        if (userDto.getId() != null) {
            throw new ConditionsException("При создании записи запрещена передача идентификатора");
        }
        if (isEmailExistsAnotherUser(userDto)) {
            throw new ConflictException("Адрес электронной почты уже используется");
        }
        var entity = repository.save(mapper.toEntityWithId(userDto));
        log.info("Создать УЗП (стоп). email: {}; id: {}", entity.getEmail(), entity.getId());
        return mapper.toDto(entity);
    }

    @Transactional
    public UserDto update(Long userId, UserDto userDto) throws NotFoundException, ConflictException {
        log.info("Редактировать УЗП (старт). id: {}", userDto.getId());
        var newUserDto = mapper.updateDto(getEntity(userId), userDto);
        if (isEmailExistsAnotherUser(newUserDto)) {
            throw new ConflictException("Адрес электронной почты уже используется");
        }
        var entity = repository.save(mapper.toEntityWithId(newUserDto));
        log.info("Редактировать УЗП (стоп). id: {}", entity.getId());
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long userId) {
        log.info("Удалить УЗП. id {}", userId);
        repository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public Users findById(Long id) throws NotFoundException {
        return repository.findById(id == null ? 0L : id)
                .orElseThrow(() -> new NotFoundException("Запись не найдена"));
    }

    @Transactional(readOnly = true)
    public UserDto getEntity(Long userId) throws NotFoundException {
        log.info("Получить описание УЗП. id {}", userId);
        return mapper.toDto(findById(userId));
    }

    @Transactional(readOnly = true)
    public Boolean isEmailExistsAnotherUser(UserDto userDto) {
        return userDto.getId() == null
                ? repository.existsByEmail(userDto.getEmail())
                : repository.existsByEmailAndIdNot(userDto.getEmail(), userDto.getId());
    }

    @Transactional(readOnly = true)
    public Boolean userIsExist(Long userId) {
        return repository.existsById(userId);
    }

}
