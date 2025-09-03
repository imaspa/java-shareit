package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingClient bookingClient;

    @Test
    void getBookings_ShouldReturnOk() throws Exception {
        when(bookingClient.getUserBookings(anyLong(), any()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header(HEADER, 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsOwner_ShouldReturnOk() throws Exception {
        when(bookingClient.getUserOwnerBookings(anyLong(), any()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                        .header(HEADER, 1)
                        .param("state", "PAST"))
                .andExpect(status().isOk());
    }

/*    @Test
    void create_ShouldReturnOk() throws Exception {
        String json = "{\n" +
                "  \"itemId\": 1,\n" +
                "  \"start\": \"2025-08-01T12:00:00\",\n" +
                "  \"end\": \"2025-08-02T12:00:00\"\n" +
                "}";

        when(bookingClient.create(anyLong(), any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/bookings")
                        .header(HEADER, 1)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }*/

    @Test
    void getBooking_ShouldReturnOk() throws Exception {
        when(bookingClient.getEntity(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/1")
                        .header(HEADER, 1))
                .andExpect(status().isOk());
    }

    @Test
    void update_ShouldReturnOk() throws Exception {
        when(bookingClient.bookingApproved(anyLong(), anyLong(), anyBoolean())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/1")
                        .header(HEADER, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }
}
