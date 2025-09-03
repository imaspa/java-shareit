package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.constant.BookingState;
import ru.practicum.shareit.booking.constant.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
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
class BookingServiceTest {
    private final BookingService service;
    private final ItemService itemService;
    private final UserService userService;
    private UserDto ownerUserDto;
    private UserDto bookerUserDto;
    private ItemDto itemDtoAvailable;
    private ItemDto itemDtoUnAvailable;


    @BeforeEach
    void setUp() throws ConditionsException, ConflictException, NotFoundException {
        UserDto ownerUserCreateDto = UserDto.builder()
                .name("no")
                .email("no@ya.ru")
                .build();
        this.ownerUserDto = userService.create(ownerUserCreateDto);

        UserDto bookerUserCreateDto = UserDto.builder()
                .name("Booker")
                .email("Booker@ya.ru")
                .build();
        this.bookerUserDto = userService.create(bookerUserCreateDto);


        ItemDto itemCreateDto = ItemDto.builder()
                .name("iName")
                .description("iDescr")
                .available(true)
                .owner(UserDto.builder().id(ownerUserDto.getId()).build())
                .build();
        this.itemDtoAvailable = itemService.create(itemCreateDto);

        ItemDto itemCreateDtoNA = ItemDto.builder()
                .name("iName")
                .description("iDescr")
                .available(false)
                .owner(UserDto.builder().id(ownerUserDto.getId()).build())
                .build();

        this.itemDtoUnAvailable = itemService.create(itemCreateDtoNA);
    }

    @Test
    void create() throws ConditionsException, NotFoundException {
        BookingInputDto bookingCreateDto = BookingInputDto.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .booker(bookerUserDto.getId())
                .itemId(itemDtoAvailable.getId())
                .build();

        BookingDto bookingDto = service.create(bookingCreateDto);

        assertThat(bookingDto.getId(), notNullValue());
        assertThat(bookingDto.getItem().getId(), equalTo(itemDtoAvailable.getId()));
        assertThat(bookingDto.getStart(), equalTo(bookingCreateDto.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(bookingCreateDto.getEnd()));
        assertThat(bookingDto.getStatus(), equalTo(BookingStatus.WAITING));
    }

    /*
    @Test
    void createWithWrongStartAndEnd() {
        LocalDateTime now = LocalDateTime.now();
        BookingInputDto bookingCreateDtoPastStart = BookingInputDto.builder()
                .start(now.minusDays(1))
                .end(now.plusMinutes(2))
                .itemId(itemDtoAvailable.getId())
                .booker(bookerUserDto.getId())
                .build();


        BookingInputDto bookingCreateDtoPastEnd = BookingInputDto.builder()
                .start(now.plusDays(1))
                .end(now.minusDays(1))
                .itemId(itemDtoAvailable.getId())
                .booker(bookerUserDto.getId())
                .build();

        BookingInputDto bookingCreateDtoStartAfterEnd = BookingInputDto.builder()
                .start(now.plusMinutes(2))
                .end(now.plusMinutes(1))
                .itemId(itemDtoAvailable.getId())
                .booker(bookerUserDto.getId())
                .build();

        BookingInputDto bookingCreateDtoStartEqualsEnd = BookingInputDto.builder()
                .start(now.plusMinutes(1))
                .end(now.plusMinutes(1))
                .itemId(itemDtoAvailable.getId())
                .booker(bookerUserDto.getId())
                .build();

        Assertions.assertThrows(ConditionsException.class,
                () -> service.create(bookingCreateDtoPastStart));
        Assertions.assertThrows(ConditionsException.class,
                () -> service.create(bookingCreateDtoPastEnd));
        Assertions.assertThrows(ConditionsException.class,
                () -> service.create(bookingCreateDtoStartAfterEnd));
        Assertions.assertThrows(ConditionsException.class,
                () -> service.create(bookingCreateDtoStartEqualsEnd));
    }

     */

    @Test
    void createWithUnAvailableItemMustThrownException() {
        LocalDateTime now = LocalDateTime.now();
        BookingInputDto bookingCreateDto = BookingInputDto.builder()
                .start(now.plusMinutes(1))
                .end(now.plusMinutes(2))
                .itemId(itemDtoUnAvailable.getId())
                .booker(bookerUserDto.getId())
                .build();

        Assertions.assertThrows(ConditionsException.class, () -> service.create(bookingCreateDto));
    }

    @Test
    void createWithUnknownItemMustThrownException() {
        LocalDateTime now = LocalDateTime.now();
        BookingInputDto bookingCreateDto = BookingInputDto.builder()
                .start(now.plusMinutes(1))
                .end(now.plusMinutes(2))
                .itemId(5L)
                .booker(bookerUserDto.getId())
                .build();

        Assertions.assertThrows(NotFoundException.class, () -> service.create(bookingCreateDto));
    }

    private BookingDto createBooking() throws ConditionsException, NotFoundException {
        LocalDateTime now = LocalDateTime.now();
        BookingInputDto bookingCreateDto = BookingInputDto.builder()
                .start(now.plusMinutes(1))
                .end(now.plusMinutes(2))
                .itemId(itemDtoAvailable.getId())
                .booker(bookerUserDto.getId())
                .build();
        return service.create(bookingCreateDto);
    }

    @Test
    void approved() throws ConditionsException, NotFoundException {
        var bookingDto = createBooking();

        BookingDto approvedBooking = service.bookingApproved(ownerUserDto.getId(), bookingDto.getId(), true);

        assertThat(approvedBooking.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void approvedWithWrongUserMustThrownException() throws ConditionsException, NotFoundException {
        var bookingDto = createBooking();

        Assertions.assertThrows(ConditionsException.class,
                () -> service.bookingApproved(bookerUserDto.getId(), bookingDto.getId(), true));
    }

    @Test
    void getBookingsByUser() throws ConditionsException, NotFoundException {
        BookingDto bookingDto = createBooking();

        List<BookingDto> listForOwner = service.getUserBookings(ownerUserDto.getId(), BookingState.ALL, false);
        List<BookingDto> listForOwnerPast = service.getUserBookings(ownerUserDto.getId(), BookingState.PAST, false);
        List<BookingDto> listForOwnerCurrent = service.getUserBookings(ownerUserDto.getId(), BookingState.CURRENT, false);
        List<BookingDto> listForOwnerFuture = service.getUserBookings(ownerUserDto.getId(), BookingState.FUTURE, false);
        List<BookingDto> listForOwnerWaiting = service.getUserBookings(ownerUserDto.getId(), BookingState.WAITING, false);
        List<BookingDto> listForOwnerRejected = service.getUserBookings(ownerUserDto.getId(), BookingState.REJECTED, false);
        List<BookingDto> listForBooker = service.getUserBookings(bookerUserDto.getId(), BookingState.ALL, false);

        assertThat(listForOwner.size(), equalTo(0));
        assertThat(listForOwnerPast.size(), equalTo(0));
        assertThat(listForOwnerCurrent.size(), equalTo(0));
        assertThat(listForOwnerWaiting.size(), equalTo(0));
        assertThat(listForOwnerFuture.size(), equalTo(0));
        assertThat(listForOwnerRejected.size(), equalTo(0));
        assertThat(listForBooker.size(), equalTo(1));
        assertThat(listForBooker.getFirst().getId(), equalTo(bookingDto.getId()));
    }

    @Test
    void getBookingByOwnerId() throws ConditionsException, NotFoundException {
        BookingDto bookingDto = createBooking();

        List<BookingDto> listForOwner = service.getUserBookings(ownerUserDto.getId(), BookingState.ALL, true);
        List<BookingDto> listForOwnerPast = service.getUserBookings(ownerUserDto.getId(), BookingState.PAST, true);
        List<BookingDto> listForOwnerCurrent = service.getUserBookings(ownerUserDto.getId(), BookingState.CURRENT, true);
        List<BookingDto> listForOwnerFuture = service.getUserBookings(ownerUserDto.getId(), BookingState.FUTURE, true);
        List<BookingDto> listForOwnerWaiting = service.getUserBookings(ownerUserDto.getId(), BookingState.WAITING, true);
        List<BookingDto> listForOwnerRejected = service.getUserBookings(ownerUserDto.getId(), BookingState.REJECTED, true);
        List<BookingDto> listForBooker = service.getUserBookings(bookerUserDto.getId(), BookingState.ALL, true);

        assertThat(listForOwner.size(), equalTo(1));
        assertThat(listForOwner.getFirst().getId(), equalTo(bookingDto.getId()));
        assertThat(listForOwnerPast.size(), equalTo(0));
        assertThat(listForOwnerCurrent.size(), equalTo(0));
        assertThat(listForOwnerWaiting.size(), equalTo(1));
        assertThat(listForOwnerFuture.size(), equalTo(1));
        assertThat(listForOwnerRejected.size(), equalTo(0));
        assertThat(listForBooker.size(), equalTo(0));
    }
}