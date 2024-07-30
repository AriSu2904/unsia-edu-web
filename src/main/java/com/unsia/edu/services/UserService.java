package com.unsia.edu.services;

import com.unsia.edu.entities.User;

public interface UserService {
    User createUser(User user);
    User getUserByEmail(String email);
}
