package ru.inurgalimov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inurgalimov.dto.CompanyDto;
import ru.inurgalimov.entity.CompanyEntity;
import ru.inurgalimov.mapper.CompanyMapper;
import ru.inurgalimov.repository.CompanyRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private final CompanyMapper mapper;

    public Collection<UUID> save(List<CompanyEntity> companies) {
        return repository.save(companies, 1000);
    }

    public CompanyEntity getById(UUID uuid) {
        return repository.findByUuid(uuid).orElse(null);
    }

    public List<CompanyDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
