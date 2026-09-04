package com.marsyushq.springchat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.marsyushq.springchat.model.Chat;


public interface ChatRepository extends MongoRepository<Chat, String>{
    
    List<Chat> findByParticipantIdsContaining(String userId);
    
}