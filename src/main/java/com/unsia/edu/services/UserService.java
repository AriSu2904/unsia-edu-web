package com.unsia.edu.services;

import com.unsia.edu.entities.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User createUser(User user);
    User getUserByEmail(String email);
    User updateUser(User user);
}
