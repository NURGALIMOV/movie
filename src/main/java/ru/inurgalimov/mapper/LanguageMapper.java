package ru.inurgalimov.mapper;

import com.google.gson.Gson;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.inurgalimov.dto.LanguageDto;
import ru.inurgalimov.entity.LanguageEntity;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface LanguageMapper {

    default List<LanguageDto> jsonToLanguage(String json, @Context Gson gson) {
        return Arrays.asList(gson.fromJson(json, LanguageDto[].class));
    }

    LanguageEntity toEntity(LanguageDto dto);

    LanguageDto toDto(LanguageEntity entity);

}
