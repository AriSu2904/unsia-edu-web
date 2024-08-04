package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.*;
import com.unsia.edu.entities.constant.EApproval;
import com.unsia.edu.entities.constant.ERole;
import com.unsia.edu.models.request.CommentRequest;
import com.unsia.edu.models.request.PostRequest;
import com.unsia.edu.models.response.CommentResponse;
import com.unsia.edu.models.response.FileResponse;
import com.unsia.edu.models.response.PostResponse;
import com.unsia.edu.repositories.PostRepository;
import com.unsia.edu.services.*;
import com.unsia.edu.utils.HelperUtil;
import com.unsia.edu.utils.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final MaterialService materialService;
    private final EntityCredentialService credentialService;
    private final CommentService commentService;
    private final ValidationUtil validationUtil;

    @Override
    public PostResponse create(PostRequest post, List<MultipartFile> multipartFiles) {
        validationUtil.validate(post);

        EntityCredential entityCredential = credentialService.extractByPrincipal();
        User user = userService.getUserByEmail(entityCredential.getEmail());

        Post newPost = Post.builder()
                .author(user)
                .title(post.getTitle())
                .content(post.getContent())
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
                    .url("/api/posts/images/" + material.getId())
                    .build();
            fileResponses.add(fileResponse);
        }

        return PostResponse.builder()
                .id(newPost.getId())
                .title(newPost.getTitle())
                .content(newPost.getContent())
                .author(HelperUtil.extractFullName(user.getFirstName(), user.getLastName()))
                .approval(newPost.getApproval().name())
                .materials(fileResponses)
                .build();
    }

    @Override
    public List<PostResponse> getPendingPosts() {
        EntityCredential credential = credentialService.extractByPrincipal();

        List<Post> listOfPosts;
        List<PostResponse> listPostResponse = new ArrayList<>();

        if(credential.getRole().equals(ERole.ROLE_USER)) {
            User user = userService.getUserByEmail(credential.getEmail());
            listOfPosts = postRepository.findAllByAuthorAndApproval(user, EApproval.APPROVAL_PENDING);
        }else {
           listOfPosts = postRepository.findAllByApproval(EApproval.APPROVAL_PENDING);
        }

        return generatePostResponse(listOfPosts, listPostResponse);
    }

    @Override
    public List<PostResponse> getPublishedPost() {
        List<Post> listOfPosts = postRepository.findAllByApproval(EApproval.APPROVAL_SUCCESS);
        List<PostResponse> listPostResponse = new ArrayList<>();

        return generatePostResponse(listOfPosts, listPostResponse);
    }

    private List<PostResponse> generatePostResponse(List<Post> listOfPosts, List<PostResponse> listPostResponse) {
        for (Post post : listOfPosts) {
            List<FileResponse> learningsMaterial = getLearningsMaterial(post);
            List<CommentResponse> comments = getComments(post);

            listPostResponse.add(PostResponse.builder()
                    .id(post.getId())
                    .author(HelperUtil.extractFullName(post.getAuthor().getFirstName(), post.getAuthor().getLastName()))
                    .title(post.getTitle())
                    .content(post.getContent())
                    .approval(post.getApproval().name())
                    .materials(learningsMaterial)
                    .comments(comments)
                    .build());
        }

        return listPostResponse;
    }

    @Override
    public PostResponse getPost(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        List<FileResponse> learningsMaterial = getLearningsMaterial(post);
        List<CommentResponse> comments = getComments(post);

        return PostResponse.builder()
                .id(post.getId())
                .author(HelperUtil.extractFullName(post.getAuthor().getFirstName(), post.getAuthor().getLastName()))
                .title(post.getTitle())
                .content(post.getContent())
                .approval(post.getApproval().name())
                .materials(learningsMaterial)
                .comments(comments)
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
                .author(HelperUtil.extractFullName(approvedPost.getAuthor().getFirstName(), approvedPost.getAuthor().getLastName()))
                .title(approvedPost.getTitle())
                .content(approvedPost.getContent())
                .approval(approvedPost.getApproval().name())
                .materials(getLearningsMaterial(approvedPost))
                .build();
    }

    @Override
    public CommentResponse postComment(String postId, CommentRequest comment) {
        validationUtil.validate(comment);

        EntityCredential credentials = credentialService.extractByPrincipal();

        User user = userService.getUserByEmail(credentials.getEmail());

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment newComment = Comment.builder()
                .content(comment.getContent())
                .author(user)
                .post(post)
                .build();

        Comment savedComment = commentService.postComment(newComment);

        return CommentResponse.builder()
                .commentId(savedComment.getId())
                .content(savedComment.getContent())
                .author(HelperUtil.extractFullName(user.getFirstName(), user.getLastName()))
                .build();
    }

    @Override
    public Resource loadPostMaterial(String materialId) {
        return materialService.loadImage(materialId);
    }

    private List<FileResponse> getLearningsMaterial(Post post) {
        List<Material> materialPost = materialService.findByPostId(post.getId());
        List<FileResponse> listMaterials = new ArrayList<>();

        for (Material material : materialPost) {
            FileResponse fileResponse = FileResponse.builder()
                    .id(material.getId())
                    .filename(material.getName())
                    .url("/api/posts/images/" + material.getId())
                    .build();
            listMaterials.add(fileResponse);
        }

        return listMaterials;
    }

    private List<CommentResponse> getComments(Post post) {
        List<Comment> commentPost = commentService.getComments(post.getId());
        List<CommentResponse> listComments = new ArrayList<>();

        for (Comment comment : commentPost) {
            CommentResponse commentResponse = CommentResponse.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .author(HelperUtil.extractFullName(comment.getAuthor().getFirstName(), comment.getAuthor().getLastName()))
                    .build();
            listComments.add(commentResponse);
        }

        return listComments;
    }

}
