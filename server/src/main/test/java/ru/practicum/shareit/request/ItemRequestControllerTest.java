package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("desc")
            .requestor(UserDto.builder().id(2L).build())
            .created(LocalDateTime.now())
            .build();
    private final ItemRequestDto itemRequestWithItemsDto = ItemRequestDto.builder()
            .id(3L)
            .description("Description")
            .created(LocalDateTime.now())
            .build();
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService service;
    @Autowired
    private MockMvc mvc;

    @Test
    void createItemRequest() throws Exception {
        ItemRequestDto input = ItemRequestDto.builder()
                .description("I'm")
                .build();


        when(service.create(any(), any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(input))
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
    }

    @Test
    void getRequestsByUser() throws Exception {
        List<ItemRequestDto> list = List.of(itemRequestWithItemsDto);
        when(service.getAllItemRequests(any())).thenReturn(list);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description").value(itemRequestWithItemsDto.getDescription()));
    }


    @Test
    void getRequestsByOtherUsers() throws Exception {
        List<ItemRequestDto> list = List.of(itemRequestDto);
        when(service.getAllItemRequestsOtherUsers(any())).thenReturn(list);
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()));
    }

    @Test
    void getRequestById() throws Exception {
        when(service.getEntity(any())).thenReturn(itemRequestWithItemsDto);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(itemRequestWithItemsDto.getDescription()));
    }


}