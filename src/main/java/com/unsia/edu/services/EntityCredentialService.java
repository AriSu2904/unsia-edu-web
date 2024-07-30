package com.unsia.edu.services;

import com.unsia.edu.entities.EntityCredential;
import com.unsia.edu.entities.constant.ERole;

public interface EntityCredentialService {
    EntityCredential createCredential(String email, String password, ERole role);
    EntityCredential findByEmail(String email);
    void isValidAuthority(ERole role);
}
