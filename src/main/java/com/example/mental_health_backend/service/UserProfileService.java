package com.example.mental_health_backend.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mental_health_backend.dto.UserProfileRequest;
import com.example.mental_health_backend.entity.MoodLog;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.entity.UserProfile;
import com.example.mental_health_backend.repository.MoodLogRepository;
import com.example.mental_health_backend.repository.UserProfileRepository;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepo;

    @Autowired
    private MoodLogRepository moodLogRepo;

    // SAVE OR UPDATE PROFILE
    public void saveOrUpdateProfile(User user, UserProfileRequest req) {

        // FETCH EXISTING PROFILE
        UserProfile profile = userProfileRepo
                .findByUserId(user.getId())
                .orElse(null);

        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        // UPDATE SAME ROW
        profile.setFirstName(req.getFirstName());
        profile.setLastName(req.getLastName());
        profile.setAge(req.getAge());
        profile.setGender(req.getGender());
        profile.setOccupation(req.getOccupation());
        profile.setStressLevel(req.getStressLevel());
        profile.setSleepHours(req.getSleepHours());

        userProfileRepo.save(profile);
    }

    // GET PROFILE BY USER
    public Optional<UserProfile> getProfileByUser(User user) {
        return userProfileRepo.findByUser(user);
    }

    public UserProfile getByUserId(Long userId) {
        return userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    // Sleep Tracking - update sleep
    public void updateSleepHours(Long userId, Integer sleepHours) {

        // 1️ Validate input
        if (sleepHours == null) {
            throw new IllegalArgumentException("Sleep hours is required");
        }

        if (sleepHours < 0 || sleepHours > 24) {
            throw new IllegalArgumentException("Sleep hours must be between 0 and 24");
        }

        // 2️ Fetch profile
        UserProfile profile = userProfileRepo
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found for userId: " + userId));

        // 3️ Update sleep
        profile.setSleepHours(sleepHours);

        // 4️ Save
        userProfileRepo.save(profile);
    }

    // USER STATUS
    public Map<String, Object> getUserStats(Long userId) {

        UserProfile profile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Integer latestMoodScore = moodLogRepo
                .findTopByUserIdOrderByLogDateDesc(userId)
                .map(MoodLog::getMoodScore)
                .orElse(0);

        Map<String, Object> stats = new HashMap<>();
        stats.put("stressLevel", profile.getStressLevel()); // LOW / MEDIUM / HIGH
        stats.put("sleepHours", profile.getSleepHours()); // number
        stats.put("moodScore", latestMoodScore); // 0–10

        return stats;
    }

}
