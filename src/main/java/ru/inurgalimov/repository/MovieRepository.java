package ru.inurgalimov.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.inurgalimov.dto.MovieDto;
import ru.inurgalimov.entity.CollectionEntity;
import ru.inurgalimov.entity.MovieEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

    private final JdbcTemplate jdbcTemplate;

    public Collection<UUID> save(List<MovieEntity> movies, int batchSize) {
        if (Objects.isNull(movies) || movies.isEmpty())
            return Collections.emptyList();
        jdbcTemplate.batchUpdate("INSERT INTO movie (uuid, id, adult, imdb_id, budget, collection, homepage, " +
                        "original_language, original_title, poster_path, overview, popularity, release_date, " +
                        "revenue, runtime, status, tagline, title, video, vote_average, vote_count) VALUES " +
                        "(uuid_generate_v4(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                movies, batchSize, (preparedStatement, entity) -> {
                    int index = 1;
                    preparedStatement.setLong(index++, entity.getId());
                    preparedStatement.setBoolean(index++, entity.isAdult());
                    preparedStatement.setString(index++, entity.getImdbId());
                    preparedStatement.setLong(index++, entity.getBudget());
                    if (Objects.nonNull(entity.getCollection())) {
                        preparedStatement.setObject(index++, entity.getCollection()
                                .getUuid(), Types.OTHER);
                    } else {
                        preparedStatement.setNull(index++, Types.OTHER);
                    }
                    preparedStatement.setString(index++, entity.getHomepage());
                    preparedStatement.setString(index++, entity.getOriginalLanguage());
                    preparedStatement.setString(index++, entity.getOriginalTitle());
                    preparedStatement.setString(index++, entity.getPosterPath());
                    preparedStatement.setString(index++, entity.getOverview());
                    preparedStatement.setFloat(index++, entity.getPopularity());
                    preparedStatement.setObject(index++, entity.getReleaseDate(), Types.DATE);
                    preparedStatement.setLong(index++, entity.getRevenue());
                    preparedStatement.setFloat(index++, entity.getRuntime());
                    preparedStatement.setString(index++, entity.getStatus());
                    preparedStatement.setString(index++, entity.getTagline());
                    preparedStatement.setString(index++, entity.getTitle());
                    preparedStatement.setBoolean(index++, entity.isVideo());
                    preparedStatement.setFloat(index++, entity.getVoteAverage());
                    preparedStatement.setLong(index++, entity.getVoteCount());
                });
        var entitiesWithUuid = findAll().stream()
                .collect(Collectors.toMap(Function.identity(), MovieEntity::getUuid));
        movies.forEach(movie -> movie.setUuid(entitiesWithUuid.getOrDefault(movie, null)));
        return entitiesWithUuid.values();
    }

    public void save(String tableName, String columnName, List<Pair> companies, int batchSize) {
        if (Objects.isNull(companies) || companies.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate(String.format("INSERT INTO %s (movie_uuid, %s) VALUES (?, ?)", tableName, columnName),
                companies, batchSize, (preparedStatement, entity) -> {
                    preparedStatement.setObject(1, entity.getLeft(), Types.OTHER);
                    preparedStatement.setObject(2, entity.getRight(), Types.OTHER);
                });
    }

    public List<UUID> findById(String tableName, RowMapper<UUID> rowMapper, UUID id) {
        return jdbcTemplate.query(String.format("SELECT * FROM %s WHERE movie_uuid = ?", tableName), rowMapper, id);
    }

    public List<MovieEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM movie", this::createMovie);
    }

    public Optional<MovieEntity> findByUuid(final UUID uuid) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM movie WHERE uuid = ?",
                    this::createMovie,
                    uuid
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<MovieEntity> getTopTwentyFilms() {
        return jdbcTemplate.query(
                "SELECT * FROM movie ORDER BY popularity DESC LIMIT 20", this::createMovie);
    }

    public List<MovieEntity> findAll(int offset, int limit) {
        return jdbcTemplate.query(String.format("SELECT * FROM movie OFFSET %d LIMIT %d", offset, limit), this::createMovie);
    }

    public Collection<MovieEntity> getTopTwentyFilmsByGenre(UUID id) {
        return jdbcTemplate.query(
                String.format("SELECT * FROM movie JOIN movie_genre on movie.uuid = movie_genre.movie_uuid " +
                        "WHERE movie_genre.genre_uuid = '%s' ORDER BY popularity DESC LIMIT 20", id),
                this::createMovie);
    }

    private MovieEntity createMovie(ResultSet rs, int rowNum) throws SQLException {
        return MovieEntity.builder()
                .uuid(rs.getObject("uuid", UUID.class))
                .id(rs.getLong("id"))
                .adult(rs.getBoolean("adult"))
                .imdbId(rs.getString("imdb_id"))
                .budget(rs.getLong("budget"))
                .collection(CollectionEntity.builder()
                        .uuid(rs.getObject("collection", UUID.class))
                        .build())
                .homepage(rs.getString("homepage"))
                .originalLanguage(rs.getString("original_language"))
                .originalTitle(rs.getString("original_title"))
                .posterPath(rs.getString("poster_path"))
                .overview(rs.getString("overview"))
                .popularity(rs.getFloat("popularity"))
                .releaseDate(rs.getObject("release_date", LocalDate.class))
                .revenue(rs.getLong("revenue"))
                .runtime(rs.getFloat("runtime"))
                .status(rs.getString("status"))
                .tagline(rs.getString("tagline"))
                .title(rs.getString("title"))
                .video(rs.getBoolean("video"))
                .voteAverage(rs.getFloat("vote_average"))
                .voteCount(rs.getLong("vote_count"))
                .build();
    }

    public Collection<MovieEntity> getFilmsByCompany(UUID uuid) {
        return jdbcTemplate.query(
                String.format("SELECT * FROM movie JOIN movie_company on movie.uuid = movie_company.movie_uuid " +
                        "WHERE movie_company.company_uuid = '%s' ORDER BY release_date LIMIT 20", uuid),
                this::createMovie);
    }

}
