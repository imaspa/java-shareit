package ru.practicum.shareit.user;


import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.core.config.CommonMapperConfiguration;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.Users;

@Mapper(config = CommonMapperConfiguration.class)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    Users map(@MappingTarget Users entity, UserDto dto);

    @Mapping(target = "id", ignore = true)
    Users toEntity(UserDto dto);

    @Mapping(target = "id", expression = "java(id)")
    Users toEntity(UserDto userDto, @Context Long id);

    UserDto toDto(Users entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    UserDto updateDto(@MappingTarget UserDto targetDto, UserDto sourceDto);
}
