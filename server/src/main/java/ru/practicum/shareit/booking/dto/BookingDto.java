package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.constant.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private BookingStatus status;
}
