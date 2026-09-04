package com.marsyushq.springchat.service;

import static org.mockito.ArgumentMatchers.any; 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.marsyushq.springchat.repository.ChatRepository;
import com.marsyushq.springchat.repository.MessageRepository;
import com.marsyushq.springchat.dto.MessageResponse;
import com.marsyushq.springchat.model.Chat;
import com.marsyushq.springchat.model.Message;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    private static final String USER_ID = "a123sdfghj";
    private static final String CHAT_ID = "chatid123";
    private static final String PARTICIPANT_ID = "asdfghj123"; 
    private static final String CONTENT_MSG = "hello world"; 

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatRepository chatRepository;

    @InjectMocks MessageService messageService;

    @Test 
    void createMessageWithUnknownChat(){
        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> messageService.createMessage(USER_ID, CHAT_ID, CONTENT_MSG)); 
    }

    @Test 
    void createMessageAsNonParticipant(){
        Chat chat = new Chat(); 
        chat.setParticipantIds(List.of(PARTICIPANT_ID));
        
        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.of(chat));

        assertThrows(ResponseStatusException.class, () -> messageService.createMessage(USER_ID, CHAT_ID, CONTENT_MSG));
    }
    
    @Test 
    void createMessageWithValidUser(){
        Chat chat = new Chat();
        Message message = new Message();
        chat.setParticipantIds(List.of(USER_ID, PARTICIPANT_ID));
        message.setSenderId(USER_ID);

        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.of(chat));
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        
        MessageResponse res = messageService.createMessage(USER_ID, CHAT_ID, CONTENT_MSG);

        assertEquals(USER_ID, res.getSenderId());
    }
    
    @Test 
    void getMessagesWithUnknownChat(){
        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> messageService.getMessages(USER_ID, CHAT_ID));
    }
    
    @Test 
    void getMessagesAsNonParticipant(){
        Chat chat = new Chat(); 
        chat.setParticipantIds(List.of(PARTICIPANT_ID));
        
        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.of(chat));

        assertThrows(ResponseStatusException.class, () -> messageService.getMessages(USER_ID, CHAT_ID));
    }
    
    @Test 
    void getMessagesWithValidUser(){
        Chat chat = new Chat();
        chat.setParticipantIds(List.of(USER_ID, PARTICIPANT_ID));
        Message message = new Message();
        message.setChatId(CHAT_ID);
        message.setContent(CONTENT_MSG);
        message.setSenderId(USER_ID);
        
        when(chatRepository.findById(CHAT_ID)).thenReturn(Optional.of(chat));
        when(messageRepository.findByChatId(CHAT_ID)).thenReturn(List.of(message));

        List<MessageResponse> res = messageService.getMessages(USER_ID, CHAT_ID);
        
        assertEquals(USER_ID, res.get(0).getSenderId());
    }
}