package ru.practicum.shareit.item;


import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.core.config.CommonMapperConfiguration;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(config = CommonMapperConfiguration.class)
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    Item map(@MappingTarget Item entity, ItemDto dto);

    @Mapping(target = "id", ignore = true)
    Item toEntity(ItemDto dto);

    @Mapping(target = "id", expression = "java(id)")
    Item toEntity(ItemDto itemDto, @Context Long id);

    ItemDto toDto(Item entity);

    @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name")
    @Mapping(target = "description")
    @Mapping(target = "available")
    ItemDto updateDto(@MappingTarget ItemDto targetDto, ItemDto sourceDto);

}
