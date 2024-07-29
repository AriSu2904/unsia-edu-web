package com.unsia.edu.services;

import com.unsia.edu.entities.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File create(MultipartFile multipartFile);
    List<File> createBulks(List<MultipartFile> multipartFiles);
    Resource get(String path);
    String delete(String path);
}
