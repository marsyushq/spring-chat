package com.marsyushq.springchat.service;

import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.marsyushq.springchat.dto.MessageResponse;
import com.marsyushq.springchat.model.Chat;
import com.marsyushq.springchat.model.Message;
import com.marsyushq.springchat.repository.ChatRepository;
import com.marsyushq.springchat.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private ChatRepository chatRepository; 

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository){
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository; 
    }

    public MessageResponse createMessage(String senderId, String chatId, String content){
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat does not exist.");
        }
        Chat existingChat = chat.get(); 
        if(!existingChat.getParticipantIds().contains(senderId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a participant in this chat");
        }
        Message newMessage = new Message();
        newMessage.setChatId(chatId);
        newMessage.setSenderId(senderId);
        newMessage.setContent(content);

        Message savedMessage = messageRepository.save(newMessage);
        return new MessageResponse(savedMessage.getId(), savedMessage.getChatId(),savedMessage.getSenderId(), savedMessage.getContent(), savedMessage.getCreatedAt());
    }

    public List<MessageResponse> getMessages(String userId, String chatId){
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat does not exist.");
        }
        Chat existingChat = chat.get();
        if(!existingChat.getParticipantIds().contains(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a participant in this chat");
        }

        return messageRepository.findByChatId(chatId)
            .stream()
            .map(msg -> new MessageResponse(msg.getId(), msg.getChatId(), msg.getSenderId(), msg.getContent(), msg.getCreatedAt()))
            .toList();
    }
}
