package ru.inurgalimov.mapper;

import com.google.gson.Gson;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.inurgalimov.dto.CollectionDto;
import ru.inurgalimov.entity.CollectionEntity;

@Mapper
public interface CollectionMapper {

    default CollectionDto jsonToCollection(String json, @Context Gson gson) {
        return gson.fromJson(json, CollectionDto.class);
    }

    CollectionEntity toEntity(CollectionDto dto);

    CollectionDto toDto(CollectionEntity entity);

}
