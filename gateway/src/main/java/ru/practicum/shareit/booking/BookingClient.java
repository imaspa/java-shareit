package ru.practicum.shareit.booking;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.constant.BookingState;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.core.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> {
                            CloseableHttpClient httpClient = HttpClients.createDefault();
                            return new HttpComponentsClientHttpRequestFactory(httpClient);
                        })
                        .build()
        );
    }

    public ResponseEntity<Object> getUserBookings(Long userId, BookingState state) {
        String path = "?state=" + state.name();
        return get(path, userId, null);
    }

    public ResponseEntity<Object> getUserOwnerBookings(Long userId, BookingState state) {
        String path = "/owner?state=" + state.name();
        return get(path, userId, null);
    }

    public ResponseEntity<Object> getEntity(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> create(Long userId, BookingInputDto bookingInputDto) {
        return post("", userId, bookingInputDto);
    }

    public ResponseEntity<Object> bookingApproved(Long userId, Long bookingId, Boolean approved) {
        String path = "/" + bookingId + "?approved=" + approved;
        return patch(path, userId, null, null);
    }

}