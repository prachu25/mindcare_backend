package com.example.mental_health_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mental_health_backend.dto.UserProfileRequest;
import com.example.mental_health_backend.entity.ChatMessage;
import com.example.mental_health_backend.entity.MentalAssessment;
import com.example.mental_health_backend.entity.MoodLog;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // GET ALL USERS
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // GET ALL CHATS
    @GetMapping("/chats")
    public List<ChatMessage> getAllChats() {
        return adminService.getAllChats();
    }

    // GET ALL MOOD
    @GetMapping("/moods")
    public List<MoodLog> getAllMoodLogs() {
        return adminService.getAllMoodLogs();
    }

    // GET ALL ASSESSMENT
    @GetMapping("/assessments")
    public List<MentalAssessment> getAllAssessments() {
        return adminService.getAllAssessments();
    }

    // GET CHAT BY USERID
    @GetMapping("/chats/user/{userId}")
    public List<ChatMessage> getChatsByUser(@PathVariable Long userId) {
        return adminService.getChatsByUser(userId);
    }

    // DELETE ALL CHATS OF USERS
    @DeleteMapping("/user/{userId}/chats")
    public ResponseEntity<String> deleteAllChatsOfUser(
            @PathVariable Long userId) {

        try {
            adminService.deleteAllChatsOfUser(userId);
            return ResponseEntity.ok("All chats of the user deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .body("No chats found for this user");
        }
    }

    // ADMIN -> CAN SEE USERS PROFILE
    @GetMapping("/users/{userId}/profile")
    public UserProfileRequest getUserProfile(@PathVariable Long userId) {
        return adminService.getUserProfile(userId);
    }

}
