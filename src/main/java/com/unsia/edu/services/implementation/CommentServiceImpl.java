package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.Comment;
import com.unsia.edu.repositories.CommentRepository;
import com.unsia.edu.services.CommentService;
import com.unsia.edu.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ValidationUtil validationUtil;

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepository.findCommentByPostId(postId);
    }

    @Override
    public Comment postComment(Comment comment) {
        validationUtil.validate(comment);

        return commentRepository.save(comment);
    }
}
