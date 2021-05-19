package ru.inurgalimov.entity;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity {

    @EqualsAndHashCode.Exclude
    private UUID uuid;
    private String iso;
    private String name;

}
