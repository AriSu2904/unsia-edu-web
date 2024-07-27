package com.unsia.edu.services.implementation;

import com.unsia.edu.entities.User;
import com.unsia.edu.repositories.UserRepository;
import com.unsia.edu.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User createUser(String firstName, String lastName, String email, String phoneNumber) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        return userRepository.save(user);
    }
}
