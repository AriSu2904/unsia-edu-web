package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.EntityCredential;
import com.unsia.edu.entities.constant.ERole;
import com.unsia.edu.models.request.LoginRequest;
import com.unsia.edu.models.request.RegisterRequest;
import com.unsia.edu.models.response.AuthenticationResponse;
import com.unsia.edu.models.response.RegisterResponse;
import com.unsia.edu.services.AuthService;
import com.unsia.edu.services.EntityCredentialService;
import com.unsia.edu.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final EntityCredentialService entityCredentialService;
    private final UserService userService;

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        EntityCredential userCredential = entityCredentialService
                .createCredential(request.getEmail(), request.getPassword(), ERole.ROLE_USER);

        userService.createUser(request.getFirstName(), request.getLastName(),
                userCredential.getEmail(), request.getPhoneNumber());

        return RegisterResponse.builder()
                .email(request.getEmail())
                .build();
    }

    @Override
    public RegisterResponse registerAdmin(RegisterRequest request) {
        return null;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request, boolean isAdmin) {
        return null;
    }
}
