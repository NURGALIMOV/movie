package ru.inurgalimov.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inurgalimov.entity.CompanyEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    public Collection<UUID> save(List<CompanyEntity> companies, int batchSize) {
        if (Objects.isNull(companies) || companies.isEmpty()) {
            return Collections.emptyList();
        }
        jdbcTemplate.batchUpdate("INSERT INTO company (uuid, id, name) values (uuid_generate_v4(), ?, ?)",
                companies, batchSize, (preparedStatement, entity) -> {
                    preparedStatement.setLong(1, entity.getId());
                    preparedStatement.setString(2, entity.getName());
                });
        var entitiesWithUuid = findAll().stream()
                .collect(Collectors.toMap(Function.identity(), CompanyEntity::getUuid));
        companies.forEach(language -> language.setUuid(entitiesWithUuid.getOrDefault(language, null)));
        return entitiesWithUuid.values();
    }

    public List<CompanyEntity> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM company",
                (rs, rowNum) -> CompanyEntity.builder()
                        .uuid(rs.getObject("uuid", UUID.class))
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .build()
        );
    }

    public Optional<CompanyEntity> findByUuid(final UUID uuid) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM company WHERE uuid = ?",
                    (rs, rowNum) -> CompanyEntity.builder()
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
