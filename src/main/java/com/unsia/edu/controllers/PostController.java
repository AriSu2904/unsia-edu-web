package com.unsia.edu.controllers;

import com.unsia.edu.entities.constant.EApproval;
import com.unsia.edu.models.common.CommonResponse;
import com.unsia.edu.models.request.PostRequest;
import com.unsia.edu.models.response.PostResponse;
import com.unsia.edu.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
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

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getPosts(@RequestParam("status") String status) {
        EApproval approval = null;
        
        if(status.equalsIgnoreCase("PUBLISHED")) {
            approval = EApproval.APPROVAL_SUCCESS;
        } else if (status.equalsIgnoreCase("PENDING")) {
            approval = EApproval.APPROVAL_PENDING;
        }

        List<PostResponse> responses = postService.getPosts(approval);

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

}
