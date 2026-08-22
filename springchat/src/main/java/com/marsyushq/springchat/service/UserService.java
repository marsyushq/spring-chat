package com.marsyushq.springchat.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marsyushq.springchat.repository.UserRepository;
import com.marsyushq.springchat.dto.CreateUserRequest;
import com.marsyushq.springchat.dto.UserResponse;
import com.marsyushq.springchat.model.User;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository; 
        this.passwordEncoder = passwordEncoder; 
    }

    public UserResponse createUser(CreateUserRequest req){
        User user = new User();

        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail());
    }
}
