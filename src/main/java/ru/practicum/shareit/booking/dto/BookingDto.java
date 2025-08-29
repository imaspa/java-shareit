package ru.practicum.shareit.booking.dto;


import ru.practicum.shareit.booking.constant.BookingStatus;

import java.time.LocalDateTime;

public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    private BookingStatus status;
}
