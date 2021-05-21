package ru.inurgalimov.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.inurgalimov.dto.CollectionDto;
import ru.inurgalimov.dto.CompanyDto;
import ru.inurgalimov.dto.GenreDto;
import ru.inurgalimov.dto.MovieDto;
import ru.inurgalimov.service.BaseService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BaseController {

    private final BaseService service;

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Pair<? extends Class<? extends Exception>, String>> upload(@RequestPart MultipartFile file) {
        return service.upload(file);
    }

    @GetMapping
    public List<MovieDto> getTopTwentyFilms() {
        return service.getTopTwentyFilms();
    }

    @GetMapping(value = "films")
    public List<MovieDto> getFilms(@RequestParam int offset, @RequestParam int limit) {
        return service.getAllMovies(offset, limit);
    }

    @GetMapping(value = "films/{id}")
    public MovieDto getFilm(@PathVariable UUID id) {
        return service.getMovieById(id);
    }

    @GetMapping(value = "collections")
    public List<CollectionDto> getCollections() {
        return service.getAllMovies();
    }

    @GetMapping(value = "collections/{id}")
    public CollectionDto getCollection(@PathVariable UUID id) {
        return service.getCollectionById(id);
    }

    @GetMapping(value = "genres")
    public List<GenreDto> getGenres() {
        return service.getAllGenres();
    }

    @GetMapping(value = "genres/{id}")
    public Collection<MovieDto> getGenre(@PathVariable UUID id) {
        return service.getTopTwentyFilmsByGenre(id);
    }

    @GetMapping(value = "companies")
    public List<CompanyDto> getCompanies() {
        return service.getAllCompany();
    }

    @GetMapping("companies/{uuid}")
    public Collection<MovieDto> getCompany(@PathVariable UUID uuid) {
        return service.getFilmsByCompany(uuid);
    }

}
