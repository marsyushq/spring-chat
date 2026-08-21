package com.marsyushq.springchat.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.marsyushq.springchat.model.User;

public interface UserRepository extends MongoRepository<User, String>{

    
} 

