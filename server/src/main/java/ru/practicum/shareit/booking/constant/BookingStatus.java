package ru.practicum.shareit.booking.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {
    WAITING("новое бронирование ожидает одобрения"),
    APPROVED("бронирование подтверждено владельцем"),
    REJECTED("бронирование отклонено владельцем"),
    CANCELED("бронирование отменено создателем");

    private final String name;
}

