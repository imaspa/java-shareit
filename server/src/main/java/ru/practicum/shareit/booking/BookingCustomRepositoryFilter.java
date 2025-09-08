package ru.practicum.shareit.booking;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.constant.BookingState;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCustomRepositoryFilter {

    private Long userId;
    private Boolean isOwner;
    private BookingState state;

}
