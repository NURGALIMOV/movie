package ru.inurgalimov.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper
public interface DateMapper {

    default LocalDate stringToLocalDate(String date) {
        return LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(date));
    }

}
