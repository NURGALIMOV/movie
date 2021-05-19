package ru.inurgalimov.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity {

    @EqualsAndHashCode.Exclude
    private UUID uuid;
    private long id;
    private boolean adult;
    private String imdbId;
    private long budget;
    @EqualsAndHashCode.Exclude
    private CollectionEntity collection;
    @EqualsAndHashCode.Exclude
    private List<GenreEntity> genres;
    private String homepage;
    private String originalLanguage;
    private String originalTitle;
    private String posterPath;
    private String overview;
    private float popularity;
    @EqualsAndHashCode.Exclude
    private List<CompanyEntity> companies;
    @EqualsAndHashCode.Exclude
    private List<CountryEntity> countries;
    private LocalDate releaseDate;
    private long revenue;
    private float runtime;
    @EqualsAndHashCode.Exclude
    private List<LanguageEntity> languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private float voteAverage;
    private long voteCount;

}

