package com.unsia.edu.repositories;

import com.unsia.edu.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}