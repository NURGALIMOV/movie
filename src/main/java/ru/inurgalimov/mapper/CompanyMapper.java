package ru.inurgalimov.mapper;

import com.google.gson.Gson;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.inurgalimov.dto.CompanyDto;
import ru.inurgalimov.entity.CompanyEntity;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface CompanyMapper {

    default List<CompanyDto> jsonToCompany(String json, @Context Gson gson) {
        return Arrays.asList(gson.fromJson(json, CompanyDto[].class));
    }

    CompanyEntity toEntity(CompanyDto dto);

    CompanyDto toDto(CompanyEntity entity);

}
