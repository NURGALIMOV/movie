package ru.inurgalimov.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntermediateDto {

    @CsvBindByName
    private long id;

    @CsvBindByName
    private boolean adult;

    @CsvBindByName(column = "imdb_id")
    private String imdbId;

    @CsvBindByName
    private long budget;

    @CsvBindByName(column = "belongs_to_collection")
    private String collection;

    @CsvBindByName
    private String genres;

    @CsvBindByName
    private String homepage;

    @CsvBindByName(column = "original_language")
    private String originalLanguage;

    @CsvBindByName(column = "original_title")
    private String originalTitle;

    @CsvBindByName(column = "poster_path")
    private String posterPath;

    @CsvBindByName
    private String overview;

    @CsvBindByName
    private float popularity;

    @CsvBindByName(column = "production_companies")
    private String companies;

    @CsvBindByName(column = "production_countries")
    private String countries;

    @CsvBindByName(column = "release_date")
    private String releaseDate;

    @CsvBindByName
    private long revenue;

    @CsvBindByName
    private float runtime;

    @CsvBindByName(column = "spoken_languages")
    private String languages;

    @CsvBindByName
    private String status;

    @CsvBindByName
    private String tagline;

    @CsvBindByName
    private String title;

    @CsvBindByName
    private boolean video;

    @CsvBindByName(column = "vote_average")
    private float voteAverage;

    @CsvBindByName(column = "vote_count")
    private long voteCount;

}
