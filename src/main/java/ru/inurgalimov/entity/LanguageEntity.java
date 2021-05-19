package ru.inurgalimov.entity;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LanguageEntity {

    @EqualsAndHashCode.Exclude
    private UUID uuid;
    private String iso;
    private String name;

}
