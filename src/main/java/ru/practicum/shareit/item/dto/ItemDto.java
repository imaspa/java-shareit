package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingLightDto;
import ru.practicum.shareit.comment.dto.CommentsDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "параметр: `Наименование` обязателен к заполнению")
    private String name;

    @NotBlank(message = "параметр: `Описание` обязателен к заполнению")
    private String description;

    @NotNull(message = "параметр: `Статус` обязателен к заполнению")
    private Boolean available;

    @NotNull(message = "параметр: `Владелец` обязателен к заполнению")
    private UserDto owner;

    private Long requestId;

    private BookingLightDto lastBooking;

    private BookingLightDto nextBooking;

    private List<CommentsDto> comments;
}
