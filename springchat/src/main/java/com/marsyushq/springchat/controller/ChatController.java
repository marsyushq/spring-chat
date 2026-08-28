package com.marsyushq.springchat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.marsyushq.springchat.service.ChatService;
import com.marsyushq.springchat.model.Chat;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping
    public List<Chat> getChats(Authentication auth){
        return chatService.getUserChats(auth.getName());
    }
}
