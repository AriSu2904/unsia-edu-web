package com.unsia.edu.services;

import com.unsia.edu.entities.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    File create(MultipartFile multipartFile);
}
