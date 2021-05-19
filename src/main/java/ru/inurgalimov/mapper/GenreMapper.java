package ru.inurgalimov.mapper;

import com.google.gson.Gson;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.inurgalimov.dto.GenreDto;
import ru.inurgalimov.entity.GenreEntity;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface GenreMapper {

    default List<GenreDto> jsonToGenre(String json, @Context Gson gson) {
        return Arrays.asList(gson.fromJson(json, GenreDto[].class));
    }

    GenreEntity toEntity(GenreDto dto);

    GenreDto toDto(GenreEntity entity);

}
