package com.unsia.edu.repositories;

import com.unsia.edu.entities.EntityCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityCredentialRepository extends JpaRepository<EntityCredential, String> {
    Optional<EntityCredential> findByEmail(String email);
}
