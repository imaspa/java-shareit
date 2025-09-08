package ru.practicum.shareit.request;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.shareit.core.config.CommonMapperConfiguration;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(config = CommonMapperConfiguration.class)
public interface ItemRequestMapper {
    @Mapping(target = "id", ignore = true)
    ItemRequest map(@MappingTarget ItemRequest entity, ItemRequestDto dto);

    ItemRequestDto map(ItemRequestDto source);

    ItemRequest toEntityWithId(ItemRequestDto dto);

    ItemRequestDto toDto(ItemRequest entity);
}
