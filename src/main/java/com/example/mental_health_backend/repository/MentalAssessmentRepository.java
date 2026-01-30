package com.example.mental_health_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.mental_health_backend.entity.MentalAssessment;
import com.example.mental_health_backend.entity.User;

@Repository
public interface MentalAssessmentRepository
        extends JpaRepository<MentalAssessment, Long> {

    List<MentalAssessment> findByUser(User user);
}
