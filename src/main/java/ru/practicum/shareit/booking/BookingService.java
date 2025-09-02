package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.constant.BookingState;
import ru.practicum.shareit.booking.constant.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository repository;
    private final BookingMapper mapper;

    private final UserService serviceUsers;
    private final ItemService serviceItem;

    @Transactional(readOnly = true)
    public List<BookingDto> getUserBookings(Long userId, BookingState state, Boolean isOwner) throws NotFoundException {
        if (!serviceUsers.userIsExist(userId)) {
            throw new NotFoundException("Собственник не найден");
        }
        var filter = new BookingCustomRepositoryFilter(userId, isOwner, state);
        var spec = BookingCustomRepository.prepareSpecification(filter);
        var sort = Sort.by(Sort.Direction.DESC, "start");

        return repository.findAll(spec, sort)
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public BookingDto getEntity(Long userId, Long bookingId) throws NotFoundException {
        log.info("Получить описание бронирования. id {}", bookingId);
        var usersDto = serviceUsers.getEntity(userId);
        var bookingBd = mapper.toDto(findById(bookingId));
//        if (isBookingAnotherUser(bookingBd.getBooker(), usersDto)) {
//            throw new NotFoundException("Просматривать возможно только свои брони");
//        }
        return bookingBd;
    }


    @Transactional
    public BookingDto create(@Valid BookingInputDto bookingInputDto) throws NotFoundException, ConditionsException {
        log.info("Создать запись с бронированием вещи (старт). id вещи: {}", bookingInputDto.getBooker());
        var itemUser = serviceUsers.getEntity(bookingInputDto.getBooker());
        var itemDto = serviceItem.getEntity(bookingInputDto.getItemId());
        if (!itemDto.getAvailable()) {
            throw new ConditionsException("Бронирование невозможно");
        }
        var bookingDto = mapper.map(bookingInputDto)
                .toBuilder()
                .booker(itemUser)
                .item(itemDto)
                .build();


        var entity = repository.save(mapper.toEntityWithId(bookingDto));
        log.info("Создать запись с бронированием вещи (стоп). Наименование: {}; id: {}", bookingInputDto.getBooker(), entity.getId());
        return mapper.toDto(entity);
    }

    @Transactional
    public BookingDto bookingApproved(Long userId, Long bookingId, Boolean approved) throws NotFoundException, ConditionsException {
        log.info("Подтверждение бронирования вещи (старт). id вещи: {}", bookingId);

        var bookingDto = mapper.toDto(findById(bookingId));
        if (isBookingAnotherUser(bookingDto.getItem().getOwner(), userId)) {
            throw new ConditionsException("Подтверждать брони возможно только на свои вещи");
        }

        bookingDto = bookingDto
                .toBuilder()
                .status((approved ? BookingStatus.APPROVED : BookingStatus.REJECTED))
                .build();
        var entity = repository.save(mapper.toEntityWithId(bookingDto));
        log.info("Подтверждение бронирования вещи (старт). id вещи: {}", bookingId);
        return mapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public Booking findById(Long id) throws NotFoundException {
        return repository.findById(id == null ? 0L : id)
                .orElseThrow(() -> new NotFoundException("Запись не найдена"));
    }

    public Boolean isBookingAnotherUser(UserDto item1, UserDto item2) {
        if (item1 == null || item2 == null) return true;
        return !Objects.equals(item1.getId(), item2.getId());
    }

    public Boolean isBookingAnotherUser(UserDto item1, Long item2) {
        if (item1 == null || item2 == null) return true;
        return !Objects.equals(item1.getId(), item2);
    }

}
