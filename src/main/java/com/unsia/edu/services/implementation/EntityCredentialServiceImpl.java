package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.EntityCredential;
import com.unsia.edu.entities.constant.ERole;
import com.unsia.edu.repositories.EntityCredentialRepository;
import com.unsia.edu.services.EntityCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EntityCredentialServiceImpl implements EntityCredentialService {

    private final EntityCredentialRepository entityCredentialRepository;
    private final PasswordEncoder bcrypt;


    @Override
    public EntityCredential createCredential(String email, String password, ERole role) {
        EntityCredential existCredential = this.findByEmail(email);
        if(existCredential != null) throw new DuplicateKeyException("Email already exist");

        String encryptedPassword = bcrypt.encode(password);

        EntityCredential entityCredential = new EntityCredential();
        entityCredential.setEmail(email);
        entityCredential.setPassword(encryptedPassword);
        entityCredential.setRole(role);

        return entityCredentialRepository.save(entityCredential);
    }

    @Override
    public EntityCredential findByEmail(String email) {
        return entityCredentialRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void isValidAuthority(ERole role) {
        EntityCredential credential = (EntityCredential)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(credential.getRole() != role) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You don't have permission to access this resource");
        }
    }

    @Override
    public EntityCredential extractByPrincipal() {
        return (EntityCredential)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}


