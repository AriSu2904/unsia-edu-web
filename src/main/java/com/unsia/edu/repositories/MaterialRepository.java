package com.unsia.edu.repositories;

import com.unsia.edu.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, String> {
    List<Material> findMaterialByPostId(String postId);
}
