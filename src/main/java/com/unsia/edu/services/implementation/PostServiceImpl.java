package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.Material;
import com.unsia.edu.entities.Post;
import com.unsia.edu.entities.User;
import com.unsia.edu.entities.constant.EApproval;
import com.unsia.edu.models.request.PostRequest;
import com.unsia.edu.models.response.FileResponse;
import com.unsia.edu.models.response.PostResponse;
import com.unsia.edu.repositories.PostRepository;
import com.unsia.edu.services.MaterialService;
import com.unsia.edu.services.PostService;
import com.unsia.edu.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserDetailsImpl userDetails;
    private final UserService userService;
    private final MaterialService materialService;

    @Override
    public PostResponse create(PostRequest post, List<MultipartFile> multipartFiles) {
        UserDetails details = userDetails.loadUserByUsername(post.getAuthor());
        User user = userService.getUserByEmail(details.getUsername());

        Post newPost = Post.builder()
                .author(user)
                .title(post.getTitle())
                .content(post.getContent())
                .author(user)
                .approval(EApproval.PENDING)
                .build();

        List<Material> materials = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            Material material = materialService.create(newPost, multipartFile);
            materials.add(material);
        }
        newPost.setMaterials(materials);

        postRepository.saveAndFlush(newPost);

        List<FileResponse> fileResponses = new ArrayList<>();
        for (Material material : materials) {
            FileResponse fileResponse = FileResponse.builder()
                    .id(material.getId())
                    .filename(material.getName())
                    .url(material.getPath())
                    .build();
            fileResponses.add(fileResponse);
        }

        return PostResponse.builder()
                .id(newPost.getId())
                .title(newPost.getTitle())
                .content(newPost.getContent())
                .author(newPost.getAuthor().getEmail())
                .approval(newPost.getApproval().name())
                .materials(fileResponses)
                .build();
    }

    @Override
    public List<PostResponse> getPosts() {
    return null;
    }
}
