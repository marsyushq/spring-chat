package com.marsyushq.springchat.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.marsyushq.springchat.repository.UserRepository;
import com.marsyushq.springchat.service.JWTService;

import io.jsonwebtoken.JwtException;

import com.marsyushq.springchat.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Security filter responsible for processing JWT authentication.
 * 
 * Gets the token from the request, finds the user, and sets
 * the user's authentication and role in Spring Security's SecurityContext.
 */
public class JWTAuthenticationFilter extends OncePerRequestFilter{

    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JWTAuthenticationFilter(JWTService jwtService, UserRepository userRepository){
        this.jwtService = jwtService; 
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException
    {
        try{
            String authHeader = req.getHeader("Authorization");
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(req, res);
                return;
            }
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);
            Optional<User> user = userRepository.findByEmail(email);
            if(user.isEmpty()){
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            User existingUser = user.get();
            Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + existingUser.getRole().name()));
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(existingUser.getEmail(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(req, res);
        } catch (JwtException e){
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}