package com.example.mental_health_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mental_health_backend.entity.ChatMessage;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.repository.ChatMessageRepository;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatRepo;

    // MAIN method
    public String handleUserMessage(User user, String userMessage) {

        // 1️ Generate next chat number
        int chatNo = generateNextChatNumber(user);

        // 2️ Generate BOT reply
        String botReply = generateBotReply(userMessage);

        // 3️ Create ONE chat record (CORRECT)
        ChatMessage chat = new ChatMessage();
        chat.setUser(user);
        chat.setConversationId("chat" + chatNo);
        chat.setUserMessage(userMessage);
        chat.setBotReply(botReply);

        chatRepo.save(chat);

        return botReply;
    }

    // HELPER METHOD (PER USER - FINAL)
    private int generateNextChatNumber(User user) {
        Integer last = chatRepo.getLastChatNumberByUser(user.getId());
        return (last == null) ? 1 : last + 1;
    }

    // SMART RULE BASE LOGIC
    private String generateBotReply(String message) {

        String msg = message.toLowerCase();

        if (msg.contains("stress") || msg.contains("pressure")) {
            return "Try deep breathing for 2 minutes or take a short walk.";
        }

        if (msg.contains("sad") || msg.contains("depressed") || msg.contains("low")) {
            return "I'm here for you. Talking to a trusted person can help.";
        }

        if (msg.contains("angry") || msg.contains("anger") || msg.contains("frustrated")) {
            return "It sounds like something upset you. Want to share what happened?";
        }

        if (msg.contains("happy") || msg.contains("good")) {
            return "That's great to hear! Keep doing what makes you happy ";
        }

        if (msg.contains("sleep")) {
            return "Good sleep is important. Try avoiding screens before bedtime.";
        }

        return "I'm listening. Tell me more about how you're feeling.";
    }

    // GET ALL CHAT
    public List<ChatMessage> getAllChats() {
        return chatRepo.findAll();
    }

    public List<ChatMessage> getChatsByUser(Long userId) {
        return chatRepo.findByUserId(userId);
    }
}
