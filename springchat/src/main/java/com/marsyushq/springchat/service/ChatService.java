package com.marsyushq.springchat.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.marsyushq.springchat.dto.ChatResponse;
import com.marsyushq.springchat.model.AccountStatus;
import com.marsyushq.springchat.model.Chat;
import com.marsyushq.springchat.repository.ChatRepository;
import com.marsyushq.springchat.repository.UserRepository;
import com.marsyushq.springchat.model.User;

import java.util.Optional;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository; 

    public ChatService(ChatRepository chatRepository, UserRepository userRepository){
        this.chatRepository = chatRepository;
        this.userRepository = userRepository; 
    }

    public List<ChatResponse> getUserChats(String userId){
        return chatRepository.findByParticipantIdsContaining(userId)
            .stream()
            .map(chat -> new ChatResponse(chat.getId(),chat.getParticipantIds(),chat.getCreatedAt(),chat.getUpdatedAt()))
            .toList(); 
    }

    public ChatResponse createChat(String currentUserId, String participantId){
        if(currentUserId.equals(participantId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot create a chat with yourself");
        }

        Optional<User> currentUser = userRepository.findById(currentUserId);
        Optional<User> participantUser = userRepository.findById(participantId);

        if(currentUser.isEmpty() || participantUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        User currentUserObj = currentUser.get();
        User participantUserObj = participantUser.get();

        if(currentUserObj.getStatus() != AccountStatus.ACTIVE || participantUserObj.getStatus() != AccountStatus.ACTIVE){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<Chat> existingChats = chatRepository.findByParticipantIdsContaining(currentUserId);

        for(Chat c : existingChats){
            if(c.getParticipantIds().contains(participantId)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Chat already exists with this user");
            }
        }
        Chat newChat = new Chat();
        newChat.setParticipantIds(List.of(currentUserId, participantId));
        Chat savedChat = chatRepository.save(newChat);

        return new ChatResponse(savedChat.getId(), savedChat.getParticipantIds(), savedChat.getCreatedAt(), savedChat.getUpdatedAt());
    }
         
}

