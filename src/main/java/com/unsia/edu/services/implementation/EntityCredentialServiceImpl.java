package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.EntityCredential;
import com.unsia.edu.entities.constant.ERole;
import com.unsia.edu.repositories.EntityCredentialRepository;
import com.unsia.edu.services.EntityCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityCredentialServiceImpl implements EntityCredentialService {

    private final EntityCredentialRepository entityCredentialRepository;
    private final PasswordEncoder bcrypt;


    @Override
    public EntityCredential createCredential(String email, String password, ERole role) {
        String encryptedPassword = bcrypt.encode(password);

        EntityCredential entityCredential = new EntityCredential();
        entityCredential.setEmail(email);
        entityCredential.setPassword(encryptedPassword);
        entityCredential.setRole(role);

        return entityCredentialRepository.save(entityCredential);
    }
}


