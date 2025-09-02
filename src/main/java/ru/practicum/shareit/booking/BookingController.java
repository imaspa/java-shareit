package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.NotFoundException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(name = "state", defaultValue = "ALL") BookingState state) throws NotFoundException {
        return bookingService.getUserBookings(userId, state, false);
    }


    @GetMapping("/owner")
    public List<BookingDto> getUserOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") BookingState state) throws NotFoundException {
        return bookingService.getUserBookings(userId, state, true);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getEntity(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @PathVariable Long bookingId) throws NotFoundException {
        return bookingService.getEntity(userId, bookingId);
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody BookingInputDto bookingInputDto) throws NotFoundException, ConditionsException {
        var bookingInputDtoNew = bookingInputDto.toBuilder().booker(userId).build();
        return bookingService.create(bookingInputDtoNew);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto bookingApproved(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam Boolean approved) throws NotFoundException, ConditionsException {
        return bookingService.bookingApproved(userId, bookingId, approved);
    }

}
