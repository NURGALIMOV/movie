package ru.inurgalimov.mapper;

import com.google.gson.Gson;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.inurgalimov.dto.IntermediateDto;
import ru.inurgalimov.dto.MovieDto;
import ru.inurgalimov.entity.MovieEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(uses = {
        CollectionMapper.class,
        GenreMapper.class,
        CompanyMapper.class,
        CountryMapper.class,
        LanguageMapper.class,
        DateMapper.class})
public interface MovieMapper {

    MovieDto toMovie(IntermediateDto intermediateData, @Context Gson gson);

    default MovieDto toMovie(IntermediateDto intermediateData, @Context Gson gson,
                             @Context List<Exception> exceptions) {
        try {
            return toMovie(intermediateData, gson);
        } catch (Exception exception) {
            exceptions.add(exception);
            return null;
        }
    }

    default List<MovieDto> toMovies(List<IntermediateDto> dataList, @Context Gson gson,
                                    @Context List<Exception> exceptions) {
        return dataList.stream()
                .map(data -> toMovie(data, gson, exceptions))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    MovieEntity toEntity(MovieDto dto);

    MovieDto toDto(MovieEntity entity);

}
