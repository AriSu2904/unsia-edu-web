package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.File;
import com.unsia.edu.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${unsia.file.dir}")
    private String path;

    @Override
    public File create(MultipartFile multipartFile) {
        log.info("incoming file {}", multipartFile.getContentType());

        if(multipartFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty");
        }

        if(!List.of("application/pdf", "image/jpg", "image/png").contains(multipartFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File type is not supported");
        }

        try {
            Path routePath = Paths.get(path);
            Files.createDirectories(routePath);
            String fileName = String.format("%d_%s",
                    System.currentTimeMillis(), multipartFile.getOriginalFilename());
            Path filePath = routePath.resolve(fileName);
            Files.copy(multipartFile.getInputStream(), filePath);

            return File.builder()
                    .name(fileName)
                    .path(filePath.toString())
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .build();
        }catch (IOException | RuntimeException e) {
            log.info("error createFile {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save file");
        }
    }

    @Override
    public List<File> createBulks(List<MultipartFile> multipartFiles) {
        return List.of();
    }

    @Override
    public Resource get(String path) {
        return null;
    }

    @Override
    public String delete(String path) {
        return "";
    }
}
