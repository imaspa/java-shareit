package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.constant.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("no")
            .email("no@ya.ru")
            .build();
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService service;
    ItemDto itemDto = ItemDto.builder()
            .id(2L)
            .name("iName")
            .description("iDescr")
            .available(true)
            .owner(userDto)
            .build();
    BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(itemDto)
            .booker(userDto)
            .status(BookingStatus.APPROVED)
            .build();
    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        BookingInputDto bookingCreateDto = BookingInputDto.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
//                .booker(bookerUserDto.getId())
                .itemId(1L)
                .build();


        when(service.create(any())).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreateDto))
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void approved() throws Exception {
        when(service.bookingApproved(any(), any(), any())).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/2")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingsByUser() throws Exception {
        List<BookingDto> list = List.of(bookingDto);
        when(service.getUserBookings(any(), any(), any())).thenReturn(list);
        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void getBookingById() throws Exception {
        when(service.getEntity(any(), any())).thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingsByOwnerId() throws Exception {
        List<BookingDto> list = List.of(bookingDto);
        when(service.getUserBookings(any(), any(), any())).thenReturn(list);
        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }


}