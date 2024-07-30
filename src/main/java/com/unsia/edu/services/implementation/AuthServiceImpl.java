package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.Admin;
import com.unsia.edu.entities.EntityCredential;
import com.unsia.edu.entities.User;
import com.unsia.edu.entities.constant.ERole;
import com.unsia.edu.models.request.AuthenticationRequest;
import com.unsia.edu.models.request.RegisterRequest;
import com.unsia.edu.models.response.AuthenticationResponse;
import com.unsia.edu.models.response.RegisterResponse;
import com.unsia.edu.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.unsia.edu.utils.ValidationUtil;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class AuthServiceImpl implements AuthService {
    private final EntityCredentialService entityCredentialService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminService adminService;
    private final ValidationUtil validationUtil;

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        validationUtil.validate(request);

       try {
           EntityCredential userCredential = entityCredentialService
                   .createCredential(request.getEmail(), request.getPassword(), ERole.ROLE_USER);

           User user = User.builder()
                   .email(userCredential.getEmail())
                   .firstName(request.getFirstName())
                   .lastName(request.getLastName())
                   .phoneNumber(request.getPhoneNumber())
                   .credential(userCredential)
                   .build();

           User createdUser = userService.createUser(user);

           return RegisterResponse.builder()
                   .email(createdUser.getEmail())
                   .build();
       }catch (DataIntegrityViolationException e) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "email already registered!");       }
    }

    @Override
    public RegisterResponse registerAdmin(RegisterRequest request) {
        validationUtil.validate(request);

       try {
           EntityCredential userCredential = entityCredentialService
                   .createCredential(request.getEmail(), request.getPassword(), ERole.ROLE_ADMIN);

           Admin newAdmin = Admin.builder()
                   .email(userCredential.getEmail())
                   .credential(userCredential)
                   .build();

           adminService.createAdmin(newAdmin);

           return RegisterResponse.builder()
                   .email(request.getEmail())
                   .build();
       }catch (DataIntegrityViolationException exception) {
           throw new ResponseStatusException(HttpStatus.CONFLICT, "email already registered!");
       }
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        validationUtil.validate(request);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            EntityCredential credential = (EntityCredential) authentication.getPrincipal();

            String token = jwtService.generateToken(credential);

            return AuthenticationResponse.builder()
                    .email(credential.getEmail())
                    .token(token)
                    .role(credential.getRole().name())
                    .build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Email or Password!");
        }
    }
}
