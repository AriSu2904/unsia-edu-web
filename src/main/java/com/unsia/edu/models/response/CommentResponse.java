package com.unsia.edu.models.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String commentId;
    private String author;
    private String content;
}
