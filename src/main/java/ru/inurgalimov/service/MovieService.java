package ru.inurgalimov.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import ru.inurgalimov.dto.MovieDto;
import ru.inurgalimov.entity.*;
import ru.inurgalimov.mapper.MovieMapper;
import ru.inurgalimov.repository.CollectionRepository;
import ru.inurgalimov.repository.MovieRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final CollectionService collectionService;
    private final CollectionRepository collectionRepository;
    private final CompanyService companyService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final LanguageService languageService;
    private final MovieMapper mapper;

    public Collection<UUID> save(List<MovieDto> movies) {
        List<MovieEntity> entities = movies.stream()
                .map(mapper::toEntity)
                .distinct()
                .collect(Collectors.toList());
        Map<MovieEntity, List<LanguageEntity>> movieWithLanguge = saveLanguage(entities);
        Map<MovieEntity, List<GenreEntity>> movieWithGenre = saveGenre(entities);
        Map<MovieEntity, List<CountryEntity>> movieWithCountry = saveCountry(entities);
        Map<MovieEntity, List<CompanyEntity>> movieWithCompany = saveCompany(entities);
        Map<CollectionEntity, List<MovieEntity>> map = entities.stream()
                .filter(movie -> Objects.nonNull(movie.getCollection()))
                .collect(Collectors.groupingBy(MovieEntity::getCollection));
        collectionService.save(map.keySet());
        map.entrySet()
                .forEach(entry -> entry.getValue()
                        .forEach(movie -> movie.getCollection()
                                .setUuid(entry.getKey()
                                        .getUuid())));
        Collection<UUID> result = movieRepository.save(entities, 1000);
        movieRepository.save("movie_company", "company_uuid", movieWithCompany.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue()
                                .stream()
                                .map(company -> Pair.of(entry.getKey()
                                        .getUuid(), company.getUuid())))
                        .collect(Collectors.toList()),
                1000);
        movieRepository.save("movie_country", "country_uuid", movieWithCountry.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue()
                                .stream()
                                .map(country -> Pair.of(entry.getKey()
                                        .getUuid(), country.getUuid())))
                        .collect(Collectors.toList()),
                1000);
        movieRepository.save("movie_language", "language_uuid", movieWithLanguge.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue()
                                .stream()
                                .map(language -> Pair.of(entry.getKey()
                                        .getUuid(), language.getUuid())))
                        .collect(Collectors.toList()),
                1000);
        movieRepository.save("movie_genre", "genre_uuid", movieWithGenre.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue()
                                .stream()
                                .map(genre -> Pair.of(entry.getKey()
                                        .getUuid(), genre.getUuid())))
                        .collect(Collectors.toList()),
                1000);
        return result;
    }

    public MovieDto getById(UUID id) {
        Optional<MovieEntity> result = movieRepository.findByUuid(id);
        if(result.isPresent()) {
            return mapper.toDto(fillMove(result.get()));
        }
        return null;
    }

    private MovieEntity fillMove(MovieEntity entity) {
        if(Objects.isNull(entity)) {
            return entity;
        }
        CollectionEntity collection = entity.getCollection();
        if (Objects.nonNull(collection)) {
            entity.setCollection(collectionRepository.findByUuid(collection.getUuid()).orElse(null));
        }
        entity.setCollection(null);

        List<CompanyEntity> companies = movieRepository.findById("movie_company",
                (rs, rowNum) -> (rs.getObject("company_uuid", UUID.class)),
                entity.getUuid())
                .stream()
                .map(companyService::getById)
                .collect(Collectors.toList());
        entity.setCompanies(companies);

        List<CountryEntity> countries = movieRepository.findById("movie_country",
                (rs, rowNum) -> (rs.getObject("country_uuid", UUID.class)),
                entity.getUuid())
                .stream()
                .map(countryService::getById)
                .collect(Collectors.toList());
        entity.setCountries(countries);

        List<LanguageEntity> languages = movieRepository.findById("movie_language",
                (rs, rowNum) -> (rs.getObject("language_uuid", UUID.class)),
                entity.getUuid())
                .stream()
                .map(languageService::getById)
                .collect(Collectors.toList());
        entity.setLanguages(languages);

        List<GenreEntity> genres = movieRepository.findById("movie_genre",
                (rs, rowNum) -> (rs.getObject("genre_uuid", UUID.class)),
                entity.getUuid())
                .stream()
                .map(genreService::getById)
                .collect(Collectors.toList());
        entity.setGenres(genres);
        return entity;
    }

    private Map<MovieEntity, List<LanguageEntity>> saveLanguage(List<MovieEntity> movies) {
        List<LanguageEntity> languages = new ArrayList<>();
        HashMap<MovieEntity, List<LanguageEntity>> result = movies.stream()
                .collect(HashMap::new,
                        (map, movie) -> fillMap(movie.getLanguages()
                                .stream(), map, movie, language -> getUniqueEntity(languages, language)),
                        HashMap::putAll);
        languageService.save(languages);
        return result;
    }

    private Map<MovieEntity, List<CompanyEntity>> saveCompany(List<MovieEntity> movies) {
        List<CompanyEntity> companies = new ArrayList<>();
        HashMap<MovieEntity, List<CompanyEntity>> result = movies.stream()
                .collect(HashMap::new,
                        (map, movie) -> fillMap(movie.getCompanies()
                                .stream(), map, movie, company -> getUniqueEntity(companies, company)),
                        HashMap::putAll);
        companyService.save(companies);
        return result;
    }

    private Map<MovieEntity, List<CountryEntity>> saveCountry(List<MovieEntity> movies) {
        List<CountryEntity> countries = new ArrayList<>();
        HashMap<MovieEntity, List<CountryEntity>> result = movies.stream()
                .collect(HashMap::new,
                        (map, movie) -> fillMap(movie.getCountries()
                                .stream(), map, movie, company -> getUniqueEntity(countries, company)),
                        HashMap::putAll);
        countryService.save(countries);
        return result;
    }

    private Map<MovieEntity, List<GenreEntity>> saveGenre(List<MovieEntity> movies) {
        List<GenreEntity> genres = new ArrayList<>();
        HashMap<MovieEntity, List<GenreEntity>> result = movies.stream()
                .collect(HashMap::new,
                        (map, movie) -> fillMap(movie.getGenres()
                                .stream(), map, movie, genre -> getUniqueEntity(genres, genre)),
                        HashMap::putAll);
        genreService.save(genres);
        return result;
    }

    private <T> T getUniqueEntity(List<T> list, T entity) {
        int index = list.indexOf(entity);
        if (index == -1) {
            list.add(entity);
            return entity;
        }
        return list.get(index);
    }

    private <T> void fillMap(Stream<T> stream, HashMap<MovieEntity, List<T>> map, MovieEntity movie,
                             Function<T, T> function) {
        map.put(movie, stream.map(function::apply)
                .collect(Collectors.toList()));
    }

    public List<MovieDto> getTopTwentyFilms() {
        return movieRepository.getTopTwentyFilms().stream().map(this::fillMove).map(mapper::toDto).collect(Collectors.toList());
    }

    public List<MovieDto> getAll(int offset, int limit) {
        return movieRepository.findAll(offset, limit).stream().map(this::fillMove).map(mapper::toDto).collect(Collectors.toList());
    }

    public Collection<MovieDto> getTopTwentyFilmsByGenre(UUID id) {
        return movieRepository.getTopTwentyFilmsByGenre(id).stream().map(this::fillMove).map(mapper::toDto).collect(Collectors.toList());
    }

    public Collection<MovieDto> getFilmsByCompany(UUID uuid) {
        return movieRepository.getFilmsByCompany(uuid).stream().map(this::fillMove).map(mapper::toDto).collect(Collectors.toList());
    }
}
