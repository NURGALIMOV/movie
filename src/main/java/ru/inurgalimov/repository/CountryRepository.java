package ru.inurgalimov.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inurgalimov.entity.CountryEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    public Collection<UUID> save(List<CountryEntity> countries, int batchSize) {
        if (Objects.isNull(countries) || countries.isEmpty())
            return Collections.emptyList();
        jdbcTemplate.batchUpdate("INSERT INTO country (uuid, iso, name) values (uuid_generate_v4(), ?, ?)",
                countries, batchSize, (preparedStatement, entity) -> {
                    preparedStatement.setString(1, entity.getIso());
                    preparedStatement.setString(2, entity.getName());
                });
        var entitiesWithUuid = findAll().stream()
                .collect(Collectors.toMap(Function.identity(), CountryEntity::getUuid));
        countries.forEach(country -> country.setUuid(entitiesWithUuid.getOrDefault(country, null)));
        return entitiesWithUuid.values();
    }

    public List<CountryEntity> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM country",
                (rs, rowNum) -> CountryEntity.builder()
                        .uuid(rs.getObject("uuid", UUID.class))
                        .iso(rs.getString("iso"))
                        .name(rs.getString("name"))
                        .build()
        );
    }

    public Optional<CountryEntity> findByUuid(final UUID uuid) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM country WHERE uuid = ?",
                    (rs, rowNum) -> CountryEntity.builder()
                            .uuid(rs.getObject("uuid", UUID.class))
                            .iso(rs.getString("iso"))
                            .name(rs.getString("name"))
                            .build(),
                    uuid
            ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

}
