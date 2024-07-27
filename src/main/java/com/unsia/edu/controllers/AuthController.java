package com.unsia.edu.controllers;

import com.unsia.edu.models.common.CommonResponse;
import com.unsia.edu.models.request.LoginRequest;
import com.unsia.edu.models.request.RegisterRequest;
import com.unsia.edu.models.response.AuthenticationResponse;
import com.unsia.edu.models.response.RegisterResponse;
import com.unsia.edu.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authService.registerUser(request);

        return ResponseEntity.ok(CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully Register!")
                .data(registerResponse)
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthenticationResponse>> register(@RequestBody LoginRequest request) {
    return null;
    }

}
