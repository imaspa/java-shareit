package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingLightDto {

    private Long id;

    private Long bookerId;

    private LocalDateTime start;

    private LocalDateTime end;
}
