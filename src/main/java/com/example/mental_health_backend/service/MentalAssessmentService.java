package com.example.mental_health_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mental_health_backend.entity.MentalAssessment;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.repository.MentalAssessmentRepository;

@Service
public class MentalAssessmentService {

    @Autowired
    private MentalAssessmentRepository assessmentRepo;

    public MentalAssessment saveAssessment(User user, MentalAssessment assessment) {

        if (assessment.getScore() < 0) {
            throw new IllegalArgumentException("Invalid score"); // IF THE SCORE NEGATIVE INVALID
        }

        assessment.setUser(user);
        assessment.setResultLevel(calculateLevel(assessment.getScore()));
        assessment.setTakenAt(LocalDateTime.now());

        return assessmentRepo.save(assessment);
    }

    // LOGIC
    private String calculateLevel(Integer score) {
        if (score <= 15)
            return "LOW";
        if (score <= 27)
            return "MODERATE";
        return "HIGH";
    }

    // GET ASSESSMENT BY USER
    public List<MentalAssessment> getAssessmentByUser(User user) {
        return assessmentRepo.findByUser(user);
    }
}
