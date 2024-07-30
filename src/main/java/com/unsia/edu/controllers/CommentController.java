package com.unsia.edu.controllers;

import com.unsia.edu.models.common.CommonResponse;
import com.unsia.edu.models.request.CommentRequest;
import com.unsia.edu.models.response.CommentResponse;
import com.unsia.edu.services.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final PostService postService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> postComment(@PathVariable String postId, @RequestBody CommentRequest comment) {
        log.info("Post comment with postId: {}", postId);
        CommentResponse response = postService.postComment(postId, comment);

        CommonResponse<CommentResponse> commonResponse = CommonResponse.<CommentResponse>builder()
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

}
