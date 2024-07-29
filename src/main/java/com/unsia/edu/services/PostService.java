package com.unsia.edu.services;

import com.unsia.edu.models.request.PostRequest;
import com.unsia.edu.models.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse create(PostRequest post, List<MultipartFile> multipartFiles);
    List<PostResponse> getPosts();
}
