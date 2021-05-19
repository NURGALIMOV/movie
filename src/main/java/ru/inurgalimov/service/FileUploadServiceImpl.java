package ru.inurgalimov.service;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.inurgalimov.dto.IntermediateDto;
import ru.inurgalimov.dto.MovieDto;
import ru.inurgalimov.mapper.MovieMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final Gson gson;
    private final MovieMapper mapper;
    private final MovieService movieService;

    @Override
    public List<Pair<? extends Class<? extends Exception>, String>> upload(MultipartFile file) throws IOException {
        List<Exception> exceptions = new ArrayList<>();
        movieService.save(parse(file, exceptions));
        return convertExceptionFromParsing(exceptions);
    }

    private List<MovieDto> parse(MultipartFile file, List<Exception> exceptions) throws IOException {
        try (var input = file.getInputStream();
             var reader = new InputStreamReader(input)) {
            CsvToBean<IntermediateDto> csvToBean = new CsvToBeanBuilder<IntermediateDto>(reader)
                    .withType(IntermediateDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false)
                    .build();
            List<IntermediateDto> intermediateData = csvToBean.parse();
            exceptions.addAll(csvToBean.getCapturedExceptions());
            return mapper.toMovies(intermediateData, gson, exceptions);
        }
    }

    private List<Pair<? extends Class<? extends Exception>, String>> convertExceptionFromParsing(List<Exception> exceptions) {
        return exceptions.stream()
                .map(exception -> {
                    if (exception instanceof CsvException) {
                        return Pair.of(exception.getClass(),
                                String.format("%s: %s", ((CsvException) exception).getLineNumber(), exception.getMessage()));
                    }
                    return Pair.of(exception.getClass(), exception.getMessage());
                })
                .collect(Collectors.toList());
    }

}
