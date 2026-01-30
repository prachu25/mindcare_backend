package com.example.mental_health_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mental_health_backend.entity.MoodLog;
import com.example.mental_health_backend.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {

    MoodLog findByUserAndLogDate(User user, LocalDate logDate);

    List<MoodLog> findByUserId(Long userId);

    List<MoodLog> findByUserIdOrderByLogDateDesc(Long userId);

    Optional<MoodLog> findTopByUserIdOrderByLogDateDesc(Long userId);

    // //mail
    // boolean existsByUserIdAndLogDate(Long userId, LocalDate logDate);

}
