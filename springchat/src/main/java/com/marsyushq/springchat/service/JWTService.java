package com.marsyushq.springchat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.marsyushq.springchat.model.User;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

/**
 * Handles JWT token generation and extraction of authentication claims.
 * 
 * Creates tokens for authenticated users and extracts
 * user information from existing tokens.
 */
@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret; 
    private final long expiration = 900000;
    
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user){
        return Jwts.builder()
            .subject(user.getEmail())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }

}
