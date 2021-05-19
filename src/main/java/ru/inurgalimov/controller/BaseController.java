package ru.inurgalimov.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.inurgalimov.dto.CollectionDto;
import ru.inurgalimov.dto.CompanyDto;
import ru.inurgalimov.dto.GenreDto;
import ru.inurgalimov.dto.MovieDto;
import ru.inurgalimov.service.*;

import java.util.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BaseController {

    private final FileUploadService uploadService;
    private final MovieService movieService;
    private final CollectionService collectionService;
    private final GenreService genreService;
    private final CompanyService companyService;

    @SneakyThrows
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pair<? extends Class<? extends Exception>, String>> upload(@RequestPart MultipartFile file) {
        return uploadService.upload(file);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MovieDto> getTopTwentyFilms() {
        return movieService.getTopTwentyFilms();
    }

    @GetMapping(value = "films", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MovieDto> getFilms(@RequestParam int offset, @RequestParam int limit) {
        return movieService.getAll(offset, limit);
    }

    @GetMapping(value = "films/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MovieDto getFilm(@PathVariable UUID id) {
        return movieService.getById(id);
    }

    @GetMapping(value = "collections", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CollectionDto> getCollections() {
        return collectionService.getAll();
    }

    @GetMapping(value = "collections/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionDto getCollection(@PathVariable UUID id) {
        return collectionService.getById(id);
    }

    @GetMapping(value = "genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GenreDto> getGenres() {
        return genreService.getAll();
    }

    @GetMapping(value = "genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<MovieDto> getGenre(@PathVariable UUID id) {
        return movieService.getTopTwentyFilmsByGenre(id);
    }

    @GetMapping(value =  "companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDto> getCompanies() {
        return companyService.getAll();
    }

    @GetMapping("companies/{uuid}")
    public Collection<MovieDto> getCompany(@PathVariable UUID uuid) {
        return movieService.getFilmsByCompany(uuid);
    }

}
