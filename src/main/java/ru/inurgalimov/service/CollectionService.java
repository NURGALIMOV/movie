package ru.inurgalimov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inurgalimov.dto.CollectionDto;
import ru.inurgalimov.entity.CollectionEntity;
import ru.inurgalimov.mapper.CollectionMapper;
import ru.inurgalimov.repository.CollectionRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository repository;
    private final CollectionMapper mapper;

    public Collection<UUID> save(Collection<CollectionEntity> collections) {
        return repository.save(collections, 1000);
    }

    public CollectionDto getById(UUID uuid) {
        return mapper.toDto(repository.findByUuid(uuid).orElse(null));
    }

    public List<CollectionDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
