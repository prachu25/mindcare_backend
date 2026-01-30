package com.example.mental_health_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mental_health_backend.dto.UserProfileRequest;
import com.example.mental_health_backend.entity.ChatMessage;
import com.example.mental_health_backend.entity.MentalAssessment;
import com.example.mental_health_backend.entity.MoodLog;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.entity.UserProfile;
import com.example.mental_health_backend.repository.ChatMessageRepository;
import com.example.mental_health_backend.repository.MentalAssessmentRepository;
import com.example.mental_health_backend.repository.MoodLogRepository;
import com.example.mental_health_backend.repository.UserProfileRepository;
import com.example.mental_health_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserProfileRepository userProfileRepo;

    @Autowired
    private ChatMessageRepository chatMessageRepo;

    @Autowired
    private MoodLogRepository moodLogRepo;

    @Autowired
    private MentalAssessmentRepository mentalAssessmentRepo;

    // USERS
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // CHATS
    public List<ChatMessage> getAllChats() {
        return chatMessageRepo.findAll();
    }

    public List<ChatMessage> getChatsByUser(Long userId) {
        return chatMessageRepo.findByUserId(userId);
    }

    // DELETE BY USERID
    @Transactional
    public void deleteAllChatsOfUser(Long userId) {

        List<ChatMessage> chats = chatMessageRepo.findByUserId(userId);

        if (chats.isEmpty()) {
            throw new RuntimeException("No chat found for this user");
        }

        chatMessageRepo.deleteByUserId(userId);
    }

    // MOODS
    public List<MoodLog> getAllMoodLogs() {
        return moodLogRepo.findAll();
    }

    // ASSESSMENTS
    public List<MentalAssessment> getAllAssessments() {
        return mentalAssessmentRepo.findAll();
    }

    // PROFILE
    public UserProfileRequest getUserProfile(Long userId) {

        UserProfile profile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        UserProfileRequest dto = new UserProfileRequest();
        dto.setUserId(profile.getUser().getId());
        dto.setAge(profile.getAge());
        dto.setGender(profile.getGender());
        dto.setOccupation(profile.getOccupation());
        dto.setStressLevel(profile.getStressLevel());
        dto.setSleepHours(profile.getSleepHours());

        return dto;
    }
}
