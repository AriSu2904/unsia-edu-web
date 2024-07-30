package com.unsia.edu.services;

import com.unsia.edu.entities.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(String postId);
    Comment postComment(Comment comment);
}
