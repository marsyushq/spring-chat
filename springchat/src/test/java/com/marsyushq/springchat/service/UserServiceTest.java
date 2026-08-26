package com.marsyushq.springchat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.marsyushq.springchat.dto.LoginResponse;
import com.marsyushq.springchat.model.AccountStatus;
import com.marsyushq.springchat.model.Role;
import com.marsyushq.springchat.model.User;
import com.marsyushq.springchat.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String EMAIL = "test@mail.com";
    private static final String PASS = "password123";
    private static final String HASHED_PASS = "hashedPassword";
    private static final String JWT_TOKEN = "fake-jwt-token";

    @Mock 
    private UserRepository userRepository;
    @Mock 
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserService userService; 

    @Test 
    void loginWithValidCredentials(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(HASHED_PASS);
        user.setRole(Role.USER);
        user.setStatus(AccountStatus.ACTIVE);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASS, HASHED_PASS)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(JWT_TOKEN);
        
        LoginResponse res = userService.login(EMAIL, PASS);

        assertEquals(JWT_TOKEN, res.getToken());
        assertEquals(EMAIL, res.getUser().getEmail());
    }
    
    @Test
    void loginWithBannedAccount(){
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(HASHED_PASS);
        user.setRole(Role.USER);
        user.setStatus(AccountStatus.BANNED);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASS, HASHED_PASS)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> userService.login(EMAIL, PASS));
    }

    @Test
    void loginWithUnknownEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        
        assertThrows(ResponseStatusException.class, () -> userService.login(EMAIL, PASS));
    }

    @Test
    void loginWithInvalidPassword(){
        User user = new User();
        
        user.setEmail(EMAIL);
        user.setPassword(HASHED_PASS);
        user.setRole(Role.USER);
        user.setStatus(AccountStatus.ACTIVE);
        
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASS, HASHED_PASS)).thenReturn(false);
        
        assertThrows(ResponseStatusException.class, () -> userService.login(EMAIL, PASS));

    }
}
