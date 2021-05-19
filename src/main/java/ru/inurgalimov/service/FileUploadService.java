package ru.inurgalimov.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {

    List<Pair<? extends Class<? extends Exception>, String>>upload(MultipartFile file) throws IOException;

}
