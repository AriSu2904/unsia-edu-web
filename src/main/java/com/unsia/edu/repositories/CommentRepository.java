package com.unsia.edu.repositories;

import com.unsia.edu.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findCommentByPostId(String postId);
}
