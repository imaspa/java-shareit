package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
//@Validated
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository repository;
    private final ItemRequestMapper mapper;

    private final UserService serviceUsers;

    @Transactional()
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) throws NotFoundException {
        log.info("Создать новый запрос вещи (старт). Пользователь id: {}", userId);
        var usersDto = serviceUsers.getEntity(userId);
        var itemRequestDtoNew = mapper.map(itemRequestDto)
                .toBuilder()
                .requestor(usersDto)
                .created(LocalDateTime.now())
                .build();
        var entity = repository.save(mapper.toEntityWithId(itemRequestDtoNew));
        log.info("Создать новый запрос вещи (стоп). Пользователь id: {}; id: {}", userId, entity.getId());
        return mapper.toDto(entity);
    }

    public List<ItemRequestDto> getAllItemRequests(Long userId) throws NotFoundException {
        log.info("Получить все свои запросы. id пользователя: {}", userId);
        if (!serviceUsers.userIsExist(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return repository.findAllByRequestor_IdOrderByCreatedDesc(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<ItemRequestDto> getAllItemRequestsOtherUsers(Long userId) {
        log.info("Получить все запросы других пользователей. id пользователя: {}", userId);
        return repository.findAllByRequestor_IdOrderByCreatedDesc(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ItemRequestDto getEntity(Long requestId) throws NotFoundException {
        log.info("Получить описание запроса. id {}", requestId);
        return mapper.toDto(findById(requestId));
    }

    @Transactional(readOnly = true)
    public ItemRequest findById(Long id) throws NotFoundException {
        return repository.findById(id == null ? 0L : id)
                .orElseThrow(() -> new NotFoundException("Запись не найдена"));
    }
}
