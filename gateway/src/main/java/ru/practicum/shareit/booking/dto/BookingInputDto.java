package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingInputDto {

    @NotNull(message = "параметр: `Дата начала` обязателен к заполнению")
    @FutureOrPresent(message = "параметр: `Дата начала` может быть только сегодняшней датой, либо будущей")
    private LocalDateTime start;

    @NotNull(message = "параметр: `Дата окончания` обязателен к заполнению")
    @FutureOrPresent(message = "параметр: `Дата окончания` может быть только датой в будущем")
    private LocalDateTime end;

    private Long itemId;

    private Long booker;
}
