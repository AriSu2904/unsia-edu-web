package com.unsia.edu.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String id;
    private String author;
    private String title;
    private String content;
    private List<FileResponse> materials;
    private List<CommentResponse> comments;
    private String approval;
}
