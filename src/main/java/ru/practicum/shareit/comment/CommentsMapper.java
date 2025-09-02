package ru.practicum.shareit.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.shareit.comment.dto.CommentsDto;
import ru.practicum.shareit.comment.model.Comments;
import ru.practicum.shareit.core.config.CommonMapperConfiguration;

@Mapper(config = CommonMapperConfiguration.class)
public interface CommentsMapper {
    @Mapping(target = "id", ignore = true)
    Comments map(@MappingTarget Comments entity, CommentsDto dto);

    Comments toEntityWithId(CommentsDto dto);

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "item", ignore = true)
    CommentsDto toDto(Comments entity);

}
