package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.constant.BookingStatus;
import ru.practicum.shareit.comment.CommentsMapper;
import ru.practicum.shareit.comment.CommentsRepository;
import ru.practicum.shareit.comment.dto.CommentsDto;
import ru.practicum.shareit.comment.dto.CommentsInputDto;
import ru.practicum.shareit.comment.model.Comments;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
//@Validated
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;

    private final CommentsRepository repositoryComments;
    private final CommentsMapper mapperComments;

    private final BookingRepository repositoryBooking;

    private final UserService serviceUsers;
    private final ItemRequestService itemRequestUsers;

    @Transactional(readOnly = true)
    public List<ItemDto> find(String searchName) {
        log.info("Получить список всех вещей. Запрос {}", searchName);
        if (StringUtils.isBlank(searchName)) {
            return Collections.emptyList();
        }
        return repository.findAvailable(searchName)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemOwner(Long userId) {
        log.info("Получить список всех вещей пользователя. id {}", userId);
        return repository.findByOwner_Id(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Item findById(Long id) throws NotFoundException {
        return repository.findById(id == null ? 0L : id)
                .orElseThrow(() -> new NotFoundException("Запись не найдена"));
    }

    @Transactional(readOnly = true)
    public ItemDto getEntity(Long userId, Long itemId) throws NotFoundException {
        log.info("Получить описание вещи. id: {}, userId: {}", itemId, userId);
        var item = getEntityCore(userId, itemId);
        return mapper.toDto(item);
    }

    @Transactional(readOnly = true)
    public ItemDto getEntity(Long itemId) throws NotFoundException {
        log.info("Получить описание вещи. id: {}", itemId);
        var item = getEntityCore(null, itemId);
        return mapper.toDto(item);
    }

    @Transactional(readOnly = true)
    public Item getEntityCore(Long userId, Long itemId) throws NotFoundException {
        var entity = findById(itemId);
        if (userId == null || !userId.equals(entity.getOwner().getId())) {
            return entity;
        }

        var lastBooking = repositoryBooking.findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now());
        var nextBooking = repositoryBooking.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now());
        return entity.toBuilder()
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();

    }

    @Transactional()
    public ItemDto create(@Valid ItemDto itemDto) throws ConditionsException, NotFoundException {
        log.info("Создать запись с описанием вещи (старт). Наименование: {}", itemDto.getName());
        var newItemDto = itemDto
                .toBuilder()
                .owner(serviceUsers.getEntity(itemDto.getOwner().getId()))
                .build();
        if (itemDto.getId() != null) {
            throw new ConditionsException("При создании записи запрещена передача идентификатора");
        }
        if (!serviceUsers.userIsExist(newItemDto.getOwner().getId())) {
            throw new NotFoundException("Собственник не найден");
        }
        var entity = mapper.toEntityWithId(newItemDto);
        if (itemDto.getRequestId() != null) {
            entity = mapper.toEntityWithId(newItemDto)
                    .toBuilder()
                    .request(itemRequestUsers.findById(itemDto.getRequestId()))
                    .build();
        }
        entity = repository.save(entity);
        log.info("Создать запись с описанием вещи (стоп). Наименование: {}; id: {}", entity.getName(), entity.getId());
        return mapper.toDto(entity);
    }

    @Transactional()
    public ItemDto update(Long itemId, ItemDto itemDto) throws NotFoundException {
        log.info("Редактировать запись с описанием вещи (старт). id: {}", itemId);
        var itemBd = getEntity(itemId);
        if (isItemAnotherUser(itemBd.getOwner(), itemDto.getOwner())) {
            throw new NotFoundException("Редактировать возможно только свои записи");
        }
        var newItemDto = mapper.updateDto(itemBd, itemDto);
        var entity = repository.save(mapper.toEntityWithId(newItemDto));
        log.info("Редактировать запись с описанием вещи (стоп).  id: {}", itemId);
        return mapper.toDto(entity);
    }

    public Boolean isItemAnotherUser(UserDto item1, UserDto item2) {
        if (item1 == null || item2 == null) return true;
        return !Objects.equals(item1.getId(), item2.getId());
    }

    @Transactional()
    public CommentsDto createComments(@Valid CommentsInputDto commentsInputDto) throws NotFoundException, ConditionsException {
        log.info("Создать запись с комментарием (старт). id вещи: {}", commentsInputDto.getItemId());
        if (!hasUserBookedItem(commentsInputDto.getItemId(), commentsInputDto.getAuthorId())) {
            throw new ConditionsException("Запрещено комментировать вещи которые не брались");
        }
        var comment = Comments.builder()
                .author(serviceUsers.findById(commentsInputDto.getAuthorId()))
                .item(findById(commentsInputDto.getItemId()))
                .text(commentsInputDto.getText())
                .created(LocalDateTime.now())
                .build();
        var entity = repositoryComments.save(comment);
        log.info("Создать запись с комментарием  (стоп). id вещи: {}; id: {}", commentsInputDto.getItemId(), entity.getId());
        return mapperComments.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Boolean hasUserBookedItem(Long itemId, Long userId) {
        return repositoryBooking.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(itemId,
                userId, LocalDateTime.now(), BookingStatus.APPROVED).isPresent();
    }
}
