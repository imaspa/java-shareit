package ru.practicum.shareit.item.dto;

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

    private String name;

    private String description;

    private Boolean available;

    private UserDto owner;

    private Long requestId;

    private BookingLightDto lastBooking;

    private BookingLightDto nextBooking;

    private List<CommentsDto> comments;
}
