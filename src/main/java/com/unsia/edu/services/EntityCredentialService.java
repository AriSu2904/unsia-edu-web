package com.unsia.edu.services;

import com.unsia.edu.entities.EntityCredential;
import com.unsia.edu.entities.constant.ERole;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

public interface EntityCredentialService {
    EntityCredential createCredential(String email, String password, ERole role);
    EntityCredential findByEmail(String email);
}
