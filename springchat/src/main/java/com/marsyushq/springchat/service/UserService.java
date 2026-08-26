package com.marsyushq.springchat.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.marsyushq.springchat.repository.UserRepository;
import com.marsyushq.springchat.dto.CreateUserRequest;
import com.marsyushq.springchat.dto.LoginResponse;
import com.marsyushq.springchat.dto.UserResponse;
import com.marsyushq.springchat.model.AccountStatus;
import com.marsyushq.springchat.model.Role;
import com.marsyushq.springchat.model.User;

import java.time.Instant;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService; 

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService){
        this.userRepository = userRepository; 
        this.passwordEncoder = passwordEncoder; 
        this.jwtService = jwtService;
    }

    public UserResponse createUser(CreateUserRequest req){
        User user = new User();

        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.USER); 
        user.setStatus(AccountStatus.ACTIVE);
        user.setCreatedAt(Instant.now());
        
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(),savedUser.getUsername(),savedUser.getEmail());
    }

    public LoginResponse login(String email, String password){
        Optional<User> user  = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        
        User existingUser = user.get();
        if(!passwordEncoder.matches(password, existingUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        if(existingUser.getStatus() != AccountStatus.ACTIVE){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        String token = jwtService.generateToken(existingUser);

        UserResponse userResponse = new UserResponse(existingUser.getId(), existingUser.getUsername(), existingUser.getEmail());
        return new LoginResponse(token, userResponse); 
    }

}
