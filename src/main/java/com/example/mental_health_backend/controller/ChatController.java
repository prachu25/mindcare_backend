package com.example.mental_health_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.mental_health_backend.entity.ChatMessage;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.service.ChatMessageService;
import com.example.mental_health_backend.service.UserService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatMessageService chatService;

    @Autowired
    private UserService userServ;

    @PostMapping("/{userId}")
    public Map<String, Object> chat(@PathVariable Long userId,
            @RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        String userMessage = request.get("message");

        // SAFETY CHECK
        if (userMessage == null || userMessage.trim().isEmpty()) {
            response.put("botReply", "Please say something ");
            return response;
        }

        User user = userServ.getUserById(userId);

        if (user == null) {
            response.put("botReply", "User not found. Please login again.");
            return response;
        }

        String botReply = chatService.handleUserMessage(user, userMessage);

        response.put("userMessage", userMessage);
        response.put("botReply", botReply);

        return response;
    }

    @GetMapping("/{userId}")
    public List<ChatMessage> getChatsByUser(@PathVariable Long userId) {
        return chatService.getChatsByUser(userId);
    }
}
