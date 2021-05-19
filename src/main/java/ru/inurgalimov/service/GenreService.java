package ru.inurgalimov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inurgalimov.dto.GenreDto;
import ru.inurgalimov.entity.CountryEntity;
import ru.inurgalimov.entity.GenreEntity;
import ru.inurgalimov.mapper.GenreMapper;
import ru.inurgalimov.repository.CountryRepository;
import ru.inurgalimov.repository.GenreRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository repository;
    private final GenreMapper mapper;

    public Collection<UUID> save(List<GenreEntity> genres) {
        return repository.save(genres, 1000);
    }

    public GenreEntity getById(UUID uuid) {
        return repository.findByUuid(uuid).orElse(null);
    }

    public List<GenreDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
