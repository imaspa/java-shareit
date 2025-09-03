package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    private final ItemService service;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final BookingService bookingService;
    private UserDto userDto;
    private UserDto secondUserDto;

    @BeforeEach
    void setUp() throws ConditionsException, ConflictException {
        UserDto userCreateDto = UserDto.builder()
                .name("no")
                .email("no@ya.ru")
                .build();
        this.userDto = userService.create(userCreateDto);

        UserDto userCreateDto2 = UserDto.builder()
                .name("no2")
                .email("no2@ya.ru")
                .build();
        this.secondUserDto = userService.create(userCreateDto2);

    }

    private ItemDto createItemFroTest() throws ConditionsException, NotFoundException {
        ItemDto itemCreateDto = ItemDto.builder()
                .name("iName")
                .description("iDescr")
                .available(true)
                .owner(UserDto.builder().id(userDto.getId()).build())
                .build();
        return service.create(itemCreateDto);

    }

    private ItemRequestDto createRequest() throws NotFoundException {
        ItemRequestDto itemRequestCreateDto = ItemRequestDto.builder()
                .description("I'm")
                .build();
        return itemRequestService.create(userDto.getId(), itemRequestCreateDto);
    }

    private BookingDto createBooking(ItemDto itemDto) throws ConditionsException, NotFoundException {
        BookingInputDto bookingCreateDto = BookingInputDto.builder()
                .start(LocalDateTime.now().plusSeconds(1))
                .end(LocalDateTime.now().plusSeconds(2))
                .booker(secondUserDto.getId())
                .itemId(itemDto.getId())
                .build();

        BookingDto bookingDto = bookingService.create(bookingCreateDto);
        return bookingService.bookingApproved(userDto.getId(), bookingDto.getId(), true);
    }

    @Test
    void create() throws ConditionsException, NotFoundException {
        ItemDto itemCreateDto = ItemDto.builder()
                .name("iName")
                .description("iDescr")
                .available(true)
                .owner(UserDto.builder().id(userDto.getId()).build())
                .build();
        ItemDto itemDto = service.create(itemCreateDto);

        assertThat(itemDto.getId(), notNullValue());
        assertThat(itemDto.getName(), equalTo(itemCreateDto.getName()));
        assertThat(itemDto.getDescription(), equalTo(itemCreateDto.getDescription()));
        assertThat(itemDto.getOwner().getId(), equalTo(userDto.getId()));
    }

    @Test
    void createWithUnknownRequestMustThrowException() {
        ItemDto itemCreateDto = ItemDto.builder()
                .name("iName")
                .description("iDescr")
                .available(true)
                .requestId(2L)
                .owner(UserDto.builder().id(userDto.getId()).build())
                .build();
        Assertions.assertThrows(NotFoundException.class, () -> service.create(itemCreateDto));
    }


    @Test
    void createWithRequest() throws NotFoundException, ConditionsException {
        ItemRequestDto itemRequestDto = createRequest();
        ItemDto itemCreateDto = ItemDto.builder()
                .name("iName")
                .description("iDescr")
                .available(true)
                .owner(UserDto.builder().id(userDto.getId()).build())
                .requestId(itemRequestDto.getId())
                .build();


        ItemDto itemDto = service.create(itemCreateDto);

        assertThat(itemDto.getId(), notNullValue());
        assertThat(itemDto.getName(), equalTo(itemCreateDto.getName()));
        assertThat(itemDto.getDescription(), equalTo(itemCreateDto.getDescription()));
        assertThat(itemDto.getOwner().getId(), equalTo(userDto.getId()));
//        assertThat(itemDto.getRequestId(), equalTo(itemRequestDto.getId()));
    }

    @Test
    void update() throws ConditionsException, NotFoundException {
        ItemDto itemDto = createItemFroTest();

        ItemDto itemUpdateDto = ItemDto.builder()
                .id(itemDto.getId())
                .owner(userDto)
                .build();
        itemUpdateDto.setName("New Name");

        ItemDto itemChangeName = service.update(itemDto.getId(), itemUpdateDto);


        itemUpdateDto.setName("");
        itemUpdateDto.setDescription("New Description");
        ItemDto itemChangeDescription = service.update(itemDto.getId(), itemUpdateDto);

        itemUpdateDto.setDescription("");
        itemUpdateDto.setAvailable(false);
        ItemDto itemChangeAvailable = service.update(itemDto.getId(), itemUpdateDto);

        assertThat(itemChangeName.getId(), equalTo(itemDto.getId()));
        assertThat(itemChangeName.getName(), equalTo("New Name"));
        assertThat(itemChangeName.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(itemChangeName.getAvailable(), equalTo(itemDto.getAvailable()));

        assertThat(itemChangeDescription.getId(), equalTo(itemDto.getId()));
        assertThat(itemChangeDescription.getDescription(), equalTo("New Description"));
        assertThat(itemChangeDescription.getAvailable(), equalTo(itemDto.getAvailable()));

        assertThat(itemChangeAvailable.getId(), equalTo(itemDto.getId()));
        assertThat(itemChangeAvailable.getAvailable(), equalTo(false));
    }

    @Test
    void updateWithUnknownItemMustThrowException() {
        ItemDto itemUpdateDto = ItemDto.builder()
                .id(1L)
                .name("Name")
                .owner(userDto)
                .build();

        Assertions.assertThrows(NotFoundException.class,
                () -> service.update(itemUpdateDto.getId(), itemUpdateDto));
    }

    @Test
    void updateWithWrongUserMustThrowException() throws ConditionsException, NotFoundException {
        ItemDto itemDto = createItemFroTest();
        ItemDto itemUpdateDto = ItemDto.builder()
                .id(itemDto.getId())
                .name("New Name")
                .owner(userDto)
                .build();


        Assertions.assertThrows(NotFoundException.class,
                () -> service.update(3L, itemUpdateDto));
//        Assertions.assertThrows(ForbiddenException.class,
//                () -> service.update(secondUserDto.getId(), itemUpdateDto));
    }


}