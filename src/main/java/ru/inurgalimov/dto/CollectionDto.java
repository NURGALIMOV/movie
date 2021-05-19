package ru.inurgalimov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDto {

    private UUID uuid;
    private long id;
    private String name;
    private String poster_path;
    private String backdrop_path;

}
