package ru.inurgalimov.mapper;

import com.google.gson.Gson;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.inurgalimov.dto.CountryDto;
import ru.inurgalimov.entity.CountryEntity;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface CountryMapper {

    default List<CountryDto> jsonToCountry(String json, @Context Gson gson) {
        return Arrays.asList(gson.fromJson(json, CountryDto[].class));
    }

    CountryEntity toEntity(CountryDto dto);

    CountryDto toDto(CountryEntity entity);

}
