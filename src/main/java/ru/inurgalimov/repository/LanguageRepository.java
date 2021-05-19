package ru.inurgalimov.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inurgalimov.entity.LanguageEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LanguageRepository {

    private final JdbcTemplate jdbcTemplate;

    public Collection<UUID> save(List<LanguageEntity> languages, int batchSize) {
        if (Objects.isNull(languages) || languages.isEmpty())
            return Collections.emptyList();
        jdbcTemplate.batchUpdate("INSERT INTO language (uuid, iso, name) values (uuid_generate_v4(), ?, ?)",
                languages, batchSize, (preparedStatement, languageEntity) -> {
                    preparedStatement.setString(1, languageEntity.getIso());
                    preparedStatement.setString(2, languageEntity.getName());
                });
        var entitiesWithUuid = findAll().stream()
                .collect(Collectors.toMap(Function.identity(),
                        LanguageEntity::getUuid));
        languages.forEach(language -> language.setUuid(entitiesWithUuid.getOrDefault(language, null)));
        return entitiesWithUuid.values();
    }

    public List<LanguageEntity> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM language",
                (rs, rowNum) -> LanguageEntity.builder()
                        .uuid(rs.getObject("uuid", UUID.class))
                        .iso(rs.getString("iso"))
                        .name(rs.getString("name"))
                        .build()
        );
    }

    public Optional<LanguageEntity> findByUuid(final UUID uuid) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM language WHERE uuid = ?",
                    (rs, rowNum) -> LanguageEntity.builder()
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
