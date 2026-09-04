package com.marsyushq.springchat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List; 

import com.marsyushq.springchat.dto.CreateMessageRequest;
import com.marsyushq.springchat.dto.MessageResponse;
import com.marsyushq.springchat.service.MessageService;

@RestController
@RequestMapping("/api/chats/{chatId}/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponse createMessage(@PathVariable String chatId, @RequestBody CreateMessageRequest req, Authentication auth){
        return messageService.createMessage(auth.getName(), chatId, req.getContent());   
    }

    @GetMapping
    public List<MessageResponse> getMessages(@PathVariable String chatId, Authentication auth){
        return messageService.getMessages(auth.getName(), chatId); 
    }
}
