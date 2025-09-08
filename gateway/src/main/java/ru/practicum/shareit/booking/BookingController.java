package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.constant.BookingState;
import ru.practicum.shareit.booking.dto.BookingInputDto;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingClient.getUserBookings(userId, state);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getUserOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingClient.getUserOwnerBookings(userId, state);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getEntity(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable Long bookingId) {
        return bookingClient.getEntity(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid BookingInputDto bookingInputDto) {
        return bookingClient.create(userId, bookingInputDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingApproved(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam Boolean approved) {
        return bookingClient.bookingApproved(userId, bookingId, approved);
    }

}
