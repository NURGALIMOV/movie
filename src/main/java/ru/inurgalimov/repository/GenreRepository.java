package ru.inurgalimov.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inurgalimov.entity.GenreEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public Collection<UUID> save(List<GenreEntity> genres, int batchSize) {
        if (Objects.isNull(genres) || genres.isEmpty())
            return Collections.emptyList();
        jdbcTemplate.batchUpdate("INSERT INTO genre (uuid, id, name) values (uuid_generate_v4(), ?, ?)",
                genres, batchSize, (preparedStatement, genreEntity) -> {
                    preparedStatement.setLong(1, genreEntity.getId());
                    preparedStatement.setString(2, genreEntity.getName());
                });
        var entitiesWithUuid = findAll().stream()
                .collect(Collectors.toMap(Function.identity(), GenreEntity::getUuid));
        genres.forEach(genre -> genre.setUuid(entitiesWithUuid.getOrDefault(genre, null)));
        return entitiesWithUuid.values();
    }

    public List<GenreEntity> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM genre",
                (rs, rowNum) -> GenreEntity.builder()
                        .uuid(rs.getObject("uuid", UUID.class))
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .build()
        );
    }

    public Optional<GenreEntity> findByUuid(final UUID uuid) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM genre WHERE uuid = ?",
                    (rs, rowNum) -> GenreEntity.builder()
                            .uuid(rs.getObject("uuid", UUID.class))
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .build(),
                    uuid
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

}
