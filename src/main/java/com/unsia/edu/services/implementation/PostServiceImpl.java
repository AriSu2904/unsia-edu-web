package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.Material;
import com.unsia.edu.entities.Post;
import com.unsia.edu.entities.User;
import com.unsia.edu.entities.constant.EApproval;
import com.unsia.edu.entities.constant.ERole;
import com.unsia.edu.models.request.PostRequest;
import com.unsia.edu.models.response.FileResponse;
import com.unsia.edu.models.response.PostResponse;
import com.unsia.edu.repositories.PostRepository;
import com.unsia.edu.services.EntityCredentialService;
import com.unsia.edu.services.MaterialService;
import com.unsia.edu.services.PostService;
import com.unsia.edu.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserDetailsImpl userDetails;
    private final UserService userService;
    private final MaterialService materialService;
    private final EntityCredentialService credentialService;

    @Override
    public PostResponse create(PostRequest post, List<MultipartFile> multipartFiles) {
        UserDetails details = userDetails.loadUserByUsername(post.getAuthor());
        User user = userService.getUserByEmail(details.getUsername());

        Post newPost = Post.builder()
                .author(user)
                .title(post.getTitle())
                .content(post.getContent())
                .author(user)
                .approval(EApproval.APPROVAL_PENDING)
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
    public List<PostResponse> getPosts(EApproval approval) {
        if(approval == EApproval.APPROVAL_PENDING) {
            credentialService.isValidAuthority(ERole.ROLE_ADMIN);
        }

        List<Post> listOfPosts = postRepository.findAllByApproval(approval);
        List<PostResponse> listPostResponse = new ArrayList<>();

        for (Post post : listOfPosts) {
            List<FileResponse> learningsMaterial = getLearningsMaterial(post);

            listPostResponse.add(PostResponse.builder()
                    .id(post.getId())
                    .author(post.getAuthor().getEmail())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .approval(post.getApproval().name())
                    .materials(learningsMaterial)
                    .build());
        }

        return listPostResponse;
    }

    @Override
    public PostResponse getPost(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        List<FileResponse> learningsMaterial = getLearningsMaterial(post);

        return PostResponse.builder()
                .id(post.getId())
                .author(post.getAuthor().getEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .approval(post.getApproval().name())
                .materials(learningsMaterial)
                .build();
    }

    @Override
    public PostResponse approvePost(String id) {
        credentialService.isValidAuthority(ERole.ROLE_ADMIN);

        log.info("Approve post with id: {}", id);

        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        );

        post.setApproval(EApproval.APPROVAL_SUCCESS);

        Post approvedPost = postRepository.saveAndFlush(post);

        return PostResponse.builder()
                .id(approvedPost.getId())
                .author(approvedPost.getAuthor().getEmail())
                .title(approvedPost.getTitle())
                .content(approvedPost.getContent())
                .approval(approvedPost.getApproval().name())
                .materials(getLearningsMaterial(approvedPost))
                .build();
    }

    private List<FileResponse> getLearningsMaterial(Post post) {
        List<Material> materialPost = materialService.findByPostId(post.getId());
        List<FileResponse> listMaterials = new ArrayList<>();

        for (Material material : materialPost) {
            FileResponse fileResponse = FileResponse.builder()
                    .id(material.getId())
                    .filename(material.getName())
                    .url(material.getPath())
                    .build();
            listMaterials.add(fileResponse);
        }

        return listMaterials;
    }

}
