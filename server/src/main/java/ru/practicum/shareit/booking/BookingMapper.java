package ru.practicum.shareit.booking;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.core.config.CommonMapperConfiguration;


@Mapper(config = CommonMapperConfiguration.class)
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);


    @Mapping(target = "id", ignore = true)
    Booking map(@MappingTarget Booking entity, BookingDto dto);

    @Mapping(target = "id", ignore = true)
    Booking toEntity(BookingDto dto);

    BookingDto toDto(Booking entity);

    Booking toEntityWithId(BookingDto dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "start")
    @Mapping(target = "end")
    @Mapping(target = "status", constant = "WAITING")
    BookingDto map(BookingInputDto source);

}
