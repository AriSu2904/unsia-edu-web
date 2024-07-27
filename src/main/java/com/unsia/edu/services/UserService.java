package com.unsia.edu.services;

import com.unsia.edu.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser (String firstName, String lastName, String email, String phoneNumber);
}
