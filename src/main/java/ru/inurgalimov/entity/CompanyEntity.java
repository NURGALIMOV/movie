package ru.inurgalimov.entity;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {

    @EqualsAndHashCode.Exclude
    private UUID uuid;
    private long id;
    private String name;

}
