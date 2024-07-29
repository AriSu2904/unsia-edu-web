package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.File;
import com.unsia.edu.entities.Material;
import com.unsia.edu.entities.Post;
import com.unsia.edu.repositories.MaterialRepository;
import com.unsia.edu.services.FileService;
import com.unsia.edu.services.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final FileService fileService;

    @Override
    public Material create(Post post, MultipartFile multipartFile) {
        File file = fileService.create(multipartFile);

        return Material.builder()
                .name(file.getName())
                .contentType(file.getContentType())
                .path(file.getPath())
                .size(file.getSize())
                .post(post)
                .build();
    }
}
