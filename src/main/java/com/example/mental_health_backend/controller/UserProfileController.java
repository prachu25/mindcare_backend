package com.example.mental_health_backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.mental_health_backend.dto.UserProfileRequest;
import com.example.mental_health_backend.entity.MentalAssessment;
import com.example.mental_health_backend.entity.MoodLog;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.entity.UserProfile;
import com.example.mental_health_backend.repository.UserProfileRepository;
import com.example.mental_health_backend.repository.UserRepository;
import com.example.mental_health_backend.service.MentalAssessmentService;
import com.example.mental_health_backend.service.MoodLogService;
import com.example.mental_health_backend.service.UserProfileService;
import com.example.mental_health_backend.service.UserService;

@RestController
@RequestMapping("/user")
public class UserProfileController {

        @Autowired
        private UserProfileService userProfileServ;

        @Autowired
        private UserService userServ;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private UserProfileRepository userProfileRepo;

        @Autowired
        private MoodLogService moodLogServ;

        @Autowired
        private MentalAssessmentService assessmentServ;

        // CREATE or UPDATE PROFILE
        @PostMapping("/profile")
        public ResponseEntity<String> createOrUpdateProfile(
                        @RequestBody UserProfileRequest req) {

                User user = userRepository.findById(req.getUserId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

                userProfileServ.saveOrUpdateProfile(user, req);

                return ResponseEntity.ok("Profile saved successfully");
        }

        @GetMapping("/profile/{userId}")
        public ResponseEntity<UserProfileRequest> getProfile(@PathVariable Long userId) {

                UserProfile profile = userProfileServ.getByUserId(userId);
                User user = profile.getUser();

                UserProfileRequest res = new UserProfileRequest();
                res.setName(user.getName());
                res.setFirstName(profile.getFirstName());
                res.setLastName(profile.getLastName());
                res.setAge(profile.getAge());
                res.setGender(profile.getGender());
                res.setOccupation(profile.getOccupation());
                res.setSleepHours(profile.getSleepHours());
                res.setStressLevel(profile.getStressLevel());

                return ResponseEntity.ok(res);
        }

        @GetMapping("/profile/exists/{userId}")
        public ResponseEntity<Boolean> profileExists(@PathVariable Long userId) {
                return ResponseEntity.ok(
                                userProfileRepo.existsByUserId(userId));
        }

        // SAVE / UPDATE TODAY'S MOOD
        @PostMapping("/mood/{userId}")
        public ResponseEntity<?> saveMood(
                        @PathVariable Long userId,
                        @RequestBody Map<String, Object> payload) {

                User user = userServ.getUserById(userId);

                // mood extract (STRING → ENUM)
                String moodStr = (String) payload.get("mood");
                String note = (String) payload.get("note"); // optional

                MoodLog.MoodType mood = MoodLog.MoodType.valueOf(moodStr);

                MoodLog moodLog = moodLogServ.saveOrUpdateMood(
                                user,
                                mood,
                                note);

                String tip = moodLogServ.generateTip(moodLog.getMood());

                return ResponseEntity.ok(
                                Map.of(
                                                "date", moodLog.getLogDate(),
                                                "mood", moodLog.getMood(),
                                                "score", moodLog.getMoodScore(),
                                                "tip", tip));
        }

        // GET TODAY'S MOOD
        @GetMapping("/mood/{userId}")
        public ResponseEntity<?> getTodayMood(@PathVariable Long userId) {

                User user = userServ.getUserById(userId);

                MoodLog moodLog = moodLogServ.getTodayMood(user);

                if (moodLog == null) {
                        return ResponseEntity.ok(
                                        Map.of("message", "No mood logged for today"));
                }

                String tip = moodLogServ.generateTip(moodLog.getMood());

                return ResponseEntity.ok(
                                Map.of(
                                                "date", moodLog.getLogDate(),
                                                "mood", moodLog.getMood(),
                                                "score", moodLog.getMoodScore(),
                                                "note", moodLog.getNote(),
                                                "tip", tip));
        }

        // MOOD HISTORY
        @GetMapping("/history/mood/{userId}")
        public ResponseEntity<?> getMoodHistory(@PathVariable Long userId) {

                User user = userServ.getUserById(userId);
                List<MoodLog> history = moodLogServ.getMoodHistory(user);

                if (history.isEmpty()) {
                        Map<String, String> msg = new HashMap<>();
                        msg.put("message", "No mood history found");
                        return ResponseEntity.ok(msg);
                }

                List<Map<String, Object>> response = new ArrayList<>();

                for (MoodLog m : history) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("date", m.getLogDate());
                        map.put("mood", m.getMood());
                        map.put("score", m.getMoodScore());
                        map.put("note", m.getNote());
                        response.add(map);
                }

                return ResponseEntity.ok(response);
        }

        // ASSESSMENT TEST
        @PostMapping("assessment/{userId}")
        public Map<String, Object> submitAssessment(
                        @PathVariable Long userId,
                        @RequestBody MentalAssessment assessment) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                MentalAssessment saved = assessmentServ.saveAssessment(user, assessment);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Assessment saved successfully");
                response.put("data", saved);

                return response;
        }

        // GET assessment history
        @GetMapping("assessment/history/{userId}")
        public Map<String, Object> getAssessments(@PathVariable Long userId) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                List<MentalAssessment> assessments = assessmentServ.getAssessmentByUser(user);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Assessment history fetched successfully");
                response.put("data", assessments);

                return response;
        }

        // Sleep Tracking - update sleep in - user_profile table -> sleep_hours
        @PutMapping("/{userId}/sleep")
        public ResponseEntity<String> updateSleep(
                        @PathVariable Long userId,
                        @RequestBody Map<String, Integer> request) {

                userProfileServ.updateSleepHours(userId, request.get("sleepHours"));

                return ResponseEntity.ok("Sleep updated successfully");
        }

        // USER STATUS API
        @GetMapping("/{userId}/status")
        public ResponseEntity<Map<String, Object>> getUserStats(
                        @PathVariable Long userId) {

                Map<String, Object> stats = userProfileServ.getUserStats(userId);
                return ResponseEntity.ok(stats);
        }

}