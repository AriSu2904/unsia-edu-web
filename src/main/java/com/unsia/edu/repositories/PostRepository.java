package com.unsia.edu.repositories;

import com.unsia.edu.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
}
