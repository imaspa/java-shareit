package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.constant.BookingState;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.core.ClientBase;


public class BookingClient extends ClientBase {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(RestTemplate rest) {
        super(rest);
    }


    public ResponseEntity<Object> getUserBookings(Long userId, BookingState state) {
        String path = API_PREFIX + "?state=" + state.name();
        return get(path, userId, null);
    }

    public ResponseEntity<Object> getUserOwnerBookings(Long userId, BookingState state) {
        String path = API_PREFIX + "/owner?state=" + state.name();
        return get(path, userId, null);
    }

    public ResponseEntity<Object> getEntity(Long userId, Long bookingId) {
        return get(API_PREFIX + "/" + bookingId, userId);
    }

    public ResponseEntity<Object> create(Long userId, BookingInputDto bookingInputDto) {
        return post(API_PREFIX, userId, bookingInputDto);
    }

    public ResponseEntity<Object> bookingApproved(Long userId, Long bookingId, Boolean approved) {
        String path = API_PREFIX + "/" + bookingId + "?approved=" + approved;
        return patch(path, userId, null, null);
    }

}