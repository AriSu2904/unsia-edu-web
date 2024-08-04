package com.unsia.edu.controllers;

import com.unsia.edu.entities.constant.EApproval;
import com.unsia.edu.models.common.CommonResponse;
import com.unsia.edu.models.request.CommentRequest;
import com.unsia.edu.models.request.PostRequest;
import com.unsia.edu.models.response.CommentResponse;
import com.unsia.edu.models.response.PostResponse;
import com.unsia.edu.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE,
                              MediaType.MULTIPART_FORM_DATA_VALUE },
                 produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> learningMaterial(@RequestPart("post") PostRequest post,
                                              @RequestPart("files") List<MultipartFile> multipartFiles) {
        PostResponse response = postService.create(post, multipartFiles);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping(value = "/pending", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getPendingPosts() {
        log.info("INCOMING REQUEST FOR PENDING POSTS");
        List<PostResponse> responses = postService.getPendingPosts();

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .data(responses)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/published")
    public ResponseEntity<?> getPublishedPosts() {
        List<PostResponse> responses = postService.getPublishedPost();

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .data(responses)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable String id) {
        PostResponse response = postService.getPost(id);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approvePost(@PathVariable String id) {
        PostResponse response = postService.approvePost(id);

        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> postComment(@PathVariable String postId, @RequestBody CommentRequest comment) {
        log.info("Post comment with postId: {}", postId);
        CommentResponse response = postService.postComment(postId, comment);

        CommonResponse<CommentResponse> commonResponse = CommonResponse.<CommentResponse>builder()
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping(
            path = "/images/{imageId}"
    )
    public ResponseEntity<?> downloadImage(@PathVariable(name = "imageId") String imageId) {
        log.info("start downloadImage");
        Resource resource = postService.loadPostMaterial(imageId);
        log.info("end downloadImage");
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
