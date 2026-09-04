package com.marsyushq.springchat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.marsyushq.springchat.service.ChatService;
import com.marsyushq.springchat.dto.CreateChatRequest;
import com.marsyushq.springchat.dto.ChatResponse;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping
    public List<ChatResponse> getChats(Authentication auth){
        return chatService.getUserChats(auth.getName());
    }

    @PostMapping
    public ChatResponse createChat(@RequestBody CreateChatRequest req, Authentication auth){
        return chatService.createChat(auth.getName(), req.getParticipantId());
    }
}
