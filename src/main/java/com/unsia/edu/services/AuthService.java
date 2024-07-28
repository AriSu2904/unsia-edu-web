package com.unsia.edu.services;

import com.unsia.edu.models.request.AuthenticationRequest;
import com.unsia.edu.models.request.RegisterRequest;
import com.unsia.edu.models.response.AuthenticationResponse;
import com.unsia.edu.models.response.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    RegisterResponse registerUser (RegisterRequest request);
    RegisterResponse registerAdmin (RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
}
