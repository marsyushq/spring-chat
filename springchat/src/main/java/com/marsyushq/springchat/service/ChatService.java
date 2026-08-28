package com.marsyushq.springchat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.marsyushq.springchat.model.Chat;
import com.marsyushq.springchat.repository.ChatRepository;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository){
        this.chatRepository = chatRepository; 
    }

    public List<Chat> getUserChats(String userId){
        return chatRepository.findByParticipantIdsContaining(userId); 
    }
}
