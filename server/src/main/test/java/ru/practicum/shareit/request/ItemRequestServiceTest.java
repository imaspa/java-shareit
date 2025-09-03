package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {
    private final ItemRequestService service;
    private final UserService userService;
    private final ItemService itemService;
    private UserDto requestorUserDto;
    private UserDto otherUserDto;

    @BeforeEach
    void setUp() throws ConditionsException, ConflictException {
        UserDto userDto = UserDto.builder()
                .name("no")
                .email("no@ya.ru")
                .build();

        this.requestorUserDto = userService.create(userDto);
        UserDto userDto1 = UserDto.builder()
                .name("no1")
                .email("no1@ya.ru")
                .build();

        this.otherUserDto = userService.create(userDto1);
    }

    @Test
    void createItemRequest() throws NotFoundException {
        ItemRequestDto input = ItemRequestDto.builder()
                .description("I'm")
                .build();

        ItemRequestDto itemRequestDto = service.create(requestorUserDto.getId(), input);

        assertThat(itemRequestDto.getId(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(input.getDescription()));
        assertThat(itemRequestDto.getRequestor().getId(), equalTo(requestorUserDto.getId()));
    }

    private ItemRequestDto createRequest() throws NotFoundException {
        ItemRequestDto input = ItemRequestDto.builder()
                .description("I'm")
                .build();

        return service.create(requestorUserDto.getId(), input);

    }

    @Test
    void getRequestsByUser() throws NotFoundException {
        ItemRequestDto itemRequestDto = createRequest();

        List<ItemRequestDto> list = service.getAllItemRequests(requestorUserDto.getId());
        List<ItemRequestDto> otherList = service.getAllItemRequests(otherUserDto.getId());

        assertThat(list.size(), equalTo(1));
        assertThat(list.getFirst().getId(), equalTo(itemRequestDto.getId()));
        assertThat(otherList.size(), equalTo(0));
    }

    @Test
    void getRequestsByOtherUsers() throws NotFoundException {
        ItemRequestDto itemRequestDto = createRequest();
        List<ItemRequestDto> otherList = service.getAllItemRequestsOtherUsers(otherUserDto.getId());
        List<ItemRequestDto> requestorList = service.getAllItemRequestsOtherUsers(requestorUserDto.getId());

        assertThat(requestorList.size(), equalTo(1));
        assertThat(requestorList.getFirst().getId(), equalTo(itemRequestDto.getId()));
        assertThat(otherList.size(), equalTo(0));
    }

    @Test
    void getRequestById() throws NotFoundException, ConditionsException {
        ItemRequestDto itemRequestDto = createRequest();

        ItemDto itemCreateDto = ItemDto.builder()
                .name("iName")
                .description("iDescr")
                .available(true)
                .owner(otherUserDto)
                .build();

        ItemDto itemDto = itemService.create(itemCreateDto);

        ItemRequestDto itemRequestWithItemsDto = service.getEntity(itemRequestDto.getId());

        assertThat(itemRequestWithItemsDto.getId(), equalTo(itemRequestDto.getId()));
//        assertThat(itemRequestWithItemsDto.getItems().size(), equalTo(1));
//        assertThat(itemRequestWithItemsDto.getItems().getFirst().getId(), equalTo(itemDto.getId()));
    }
}