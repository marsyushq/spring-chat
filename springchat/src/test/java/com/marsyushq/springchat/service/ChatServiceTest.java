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
import com.marsyushq.springchat.repository.UserRepository;
import com.marsyushq.springchat.model.AccountStatus;
import com.marsyushq.springchat.model.Chat;
import com.marsyushq.springchat.model.User;
import com.marsyushq.springchat.dto.ChatResponse;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    
    private static final String USER_ID = "a123sdfghj";
    private static final String CHAT_ID = "chatid123";
    private static final String PARTICIPANT_ID = "asdfghj123"; 

    @Mock
    private ChatRepository chatRepository;
    @Mock 
    private UserRepository userRepository; 

    @InjectMocks
    private ChatService chatService;

    @Test 
    void createChatWithValidUsers(){
        User user1 = new User();
        User user2 = new User();
        Chat chat = new Chat(); 
        user1.setStatus(AccountStatus.ACTIVE);
        user2.setStatus(AccountStatus.ACTIVE);
        chat.setId(CHAT_ID);
        chat.setParticipantIds(List.of(USER_ID, PARTICIPANT_ID));

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user1));
        when(userRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.of(user2));
        when(chatRepository.findByParticipantIdsContaining(USER_ID)).thenReturn(List.of());
        when(chatRepository.save(any(Chat.class))).thenReturn(chat); 
        
        ChatResponse res = chatService.createChat(USER_ID, PARTICIPANT_ID);

        assertEquals(CHAT_ID, res.getId());
    }

    @Test 
    void createChatWithSelf(){
        assertThrows(ResponseStatusException.class, () -> chatService.createChat(USER_ID, USER_ID));
    }

    @Test 
    void createChatWithUnknownUser(){
        User user = new User(); 

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user)); 
        when(userRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> chatService.createChat(USER_ID, PARTICIPANT_ID));
    }

    @Test 
    void createChatWithInactiveUser(){
        User user1 = new User();
        User user2 = new User();
        user1.setStatus(AccountStatus.ACTIVE);
        user2.setStatus(AccountStatus.DEACTIVATED);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user1));
        when(userRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.of(user2));

        assertThrows(ResponseStatusException.class, () -> chatService.createChat(USER_ID, PARTICIPANT_ID));
    }

    @Test 
    void createChatWithBannedUser(){
        User user1 = new User();
        User user2 = new User();
        user1.setStatus(AccountStatus.ACTIVE);
        user2.setStatus(AccountStatus.BANNED);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user1));
        when(userRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.of(user2));

        assertThrows(ResponseStatusException.class, () -> chatService.createChat(USER_ID, PARTICIPANT_ID));
    }

    @Test 
    void createChatThatAlreadyExists(){
        User user1 = new User();
        User user2 = new User();
        Chat chat = new Chat();
        user1.setStatus(AccountStatus.ACTIVE);
        user2.setStatus(AccountStatus.ACTIVE);
        chat.setParticipantIds(List.of(USER_ID, PARTICIPANT_ID));

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user1));
        when(userRepository.findById(PARTICIPANT_ID)).thenReturn(Optional.of(user2));
        when(chatRepository.findByParticipantIdsContaining(USER_ID)).thenReturn(List.of(chat));

        assertThrows(ResponseStatusException.class, () -> chatService.createChat(USER_ID, PARTICIPANT_ID));
    }

    @Test 
    void getUserChats(){
        Chat chat = new Chat();
        chat.setId(CHAT_ID);
        chat.setParticipantIds(List.of(USER_ID, PARTICIPANT_ID));

        when(chatRepository.findByParticipantIdsContaining(USER_ID)).thenReturn(List.of(chat));
        List<ChatResponse> res = chatService.getUserChats(USER_ID);

        assertEquals(CHAT_ID, res.get(0).getId());
    }
}
