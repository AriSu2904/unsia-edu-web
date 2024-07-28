package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.User;
import com.unsia.edu.repositories.UserRepository;
import com.unsia.edu.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User createUser(User user) {
        User existUser = this.getUserByEmail(user.getEmail());
        if(existUser != null) throw new DuplicateKeyException("Email already exist");

        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
