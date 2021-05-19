package ru.inurgalimov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inurgalimov.entity.LanguageEntity;
import ru.inurgalimov.repository.LanguageRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository repository;

    public Collection<UUID> save(List<LanguageEntity> languages) {
        return repository.save(languages, 1000);
    }

    public LanguageEntity getById(UUID uuid) {
        return repository.findByUuid(uuid).orElse(null);
    }
}
