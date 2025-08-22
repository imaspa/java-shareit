package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.core.config.CommonMapperConfiguration;

@Mapper(config = CommonMapperConfiguration.class)
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    Booking map(@MappingTarget Booking entity, BookingDto dto);

    @Mapping(target = "id", ignore = true)
    Booking toEntity(BookingDto dto);

    BookingDto toDto(Booking entity);
}
