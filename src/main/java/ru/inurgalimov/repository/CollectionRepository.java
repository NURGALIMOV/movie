package ru.inurgalimov.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inurgalimov.entity.CollectionEntity;
import ru.inurgalimov.entity.MovieEntity;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CollectionRepository {

    private final JdbcTemplate jdbcTemplate;

    public Collection<UUID> save(Collection<CollectionEntity> collections, int batchSize) {
        if (Objects.isNull(collections) || collections.isEmpty()) {
            return Collections.emptyList();
        }
        jdbcTemplate.batchUpdate("INSERT INTO collection (uuid, id, name, poster_path, backdrop_path) " +
                        "values (uuid_generate_v4(), ?, ?, ?, ?)",
                collections, batchSize, (preparedStatement, entity) -> {
                    preparedStatement.setLong(1, entity.getId());
                    preparedStatement.setString(2, entity.getName());
                    preparedStatement.setString(3, entity.getPosterPath());
                    preparedStatement.setString(4, entity.getBackdropPath());
                });
        var entitiesWithUuid = findAll().stream()
                .collect(Collectors.toMap(Function.identity(), CollectionEntity::getUuid));
        collections.forEach(collection -> collection.setUuid(entitiesWithUuid.getOrDefault(collection, null)));
        return entitiesWithUuid.values();
    }

    public List<CollectionEntity> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM collection", (rs, rowNum) -> CollectionEntity.builder()
                        .uuid(rs.getObject("uuid", UUID.class))
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .posterPath(rs.getString("poster_path"))
                        .backdropPath(rs.getString("backdrop_path"))
                        .build()
        );
    }

    public Optional<CollectionEntity> findByUuid(final UUID uuid) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM collection WHERE uuid = ?",
                    (rs, rowNum) -> CollectionEntity.builder()
                            .uuid(rs.getObject("uuid", UUID.class))
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .posterPath(rs.getString("poster_path"))
                            .backdropPath(rs.getString("backdrop_path"))
                            .build(),
                    uuid
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

}
