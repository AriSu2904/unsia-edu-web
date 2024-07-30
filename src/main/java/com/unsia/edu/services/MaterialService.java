package com.unsia.edu.services;

import com.unsia.edu.entities.Material;
import com.unsia.edu.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MaterialService {
    Material create(Post post, MultipartFile multipartFile);
    List<Material> findByPostId(String postId);
}
