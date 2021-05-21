package ru.inurgalimov.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.inurgalimov.dto.CollectionDto;
import ru.inurgalimov.dto.CompanyDto;
import ru.inurgalimov.dto.GenreDto;
import ru.inurgalimov.dto.MovieDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BaseService {

    private final FileUploadService uploadService;
    private final MovieService movieService;
    private final CollectionService collectionService;
    private final GenreService genreService;
    private final CompanyService companyService;

    @SneakyThrows
    public List<Pair<? extends Class<? extends Exception>, String>> upload(MultipartFile file) {
        return uploadService.upload(file);
    }

    public List<MovieDto> getTopTwentyFilms() {
        return movieService.getTopTwentyFilms();
    }

    public List<MovieDto> getAllMovies(int offset, int limit) {
        return movieService.getAll(offset, limit);
    }

    public List<CollectionDto> getAllMovies() {
        return collectionService.getAll();
    }

    public MovieDto getMovieById(UUID id) {
        return movieService.getById(id);
    }

    public CollectionDto getCollectionById(UUID id) {
        return collectionService.getById(id);
    }

    public List<GenreDto> getAllGenres() {
        return genreService.getAll();
    }

    public Collection<MovieDto> getTopTwentyFilmsByGenre(UUID id) {
        return movieService.getTopTwentyFilmsByGenre(id);
    }

    public List<CompanyDto> getAllCompany() {
        return companyService.getAll();
    }

    public Collection<MovieDto> getFilmsByCompany(UUID uuid) {
        return movieService.getFilmsByCompany(uuid);
    }

}
