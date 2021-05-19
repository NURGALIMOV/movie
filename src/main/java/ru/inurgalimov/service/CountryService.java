package ru.inurgalimov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inurgalimov.entity.CompanyEntity;
import ru.inurgalimov.entity.CountryEntity;
import ru.inurgalimov.repository.CompanyRepository;
import ru.inurgalimov.repository.CountryRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository repository;

    public Collection<UUID> save(List<CountryEntity> countries) {
        return repository.save(countries, 1000);
    }

    public CountryEntity getById(UUID uuid) {
        return repository.findByUuid(uuid).orElse(null);
    }
}
