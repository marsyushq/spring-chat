package com.marsyushq.springchat.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marsyushq.springchat.dto.CreateUserRequest;
import com.marsyushq.springchat.dto.UserResponse;
import com.marsyushq.springchat.service.UserService;
import com.marsyushq.springchat.dto.LoginRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest req){
        return userService.createUser(req); 
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest req){
        return userService.login(req.getEmail(), req.getPassword());
    }
}
