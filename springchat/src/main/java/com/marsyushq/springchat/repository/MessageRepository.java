package com.marsyushq.springchat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.marsyushq.springchat.model.Message;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String>{
    List<Message> findByChatId(String chatId);
}
