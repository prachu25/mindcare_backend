package com.example.mental_health_backend.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mental_health_backend.entity.MoodLog;
import com.example.mental_health_backend.entity.User;
import com.example.mental_health_backend.repository.MoodLogRepository;

@Service
public class MoodLogService {

    @Autowired
    private MoodLogRepository moodLogRepo;

    // MAIN METHOD
    public MoodLog saveOrUpdateMood(User user, MoodLog.MoodType mood, String note) {

        LocalDate today = LocalDate.now();

        MoodLog moodLog = moodLogRepo.findByUserAndLogDate(user, today);

        if (moodLog == null) {
            moodLog = new MoodLog();
            moodLog.setUser(user);
            moodLog.setLogDate(today);
        }

        // CORE LOGIC
        moodLog.setMood(mood);
        moodLog.setMoodScore(mapMoodToScore(mood));
        moodLog.setNote(note); // optional

        return moodLogRepo.save(moodLog);
    }

    // Mood → Score mapping
    private int mapMoodToScore(MoodLog.MoodType mood) {

        switch (mood) {
            case SAD:
                return 2;
            case ANXIOUS:
                return 4;
            case STRESSED:
                return 5;
            case NEUTRAL:
                return 6;
            case CALM:
                return 8;
            case HAPPY:
                return 9;
            case ECSTATIC:
                return 10;
            case ANGRY:
                return 3;
            default:
                return 6;
        }
    }

    // Tips generation (based on mood, NOT score)
    public String generateTip(MoodLog.MoodType mood) {

        switch (mood) {
            case SAD:
                return "It’s okay to feel low. Reach out to someone you trust.";
            case ANXIOUS:
                return "Pause and take slow deep breaths for a minute.";
            case STRESSED:
                return "Feeling under pressure — let’s slow down and breathe.";
            case ANGRY:
                return "Step away for a moment and let the intensity settle.";
            case CALM:
                return "Maintain this calm with mindfulness or a short walk.";
            case HAPPY:
                return "Celebrate this positive moment!";
            case ECSTATIC:
                return "Channel this energy into something creative.";
            default:
                return "Take care of yourself today.";
        }
    }

    // FETCH TODAYS MOOD
    public MoodLog getTodayMood(User user) {
        return moodLogRepo.findByUserAndLogDate(user, LocalDate.now());
    }

    // FULL MOOD HISTORY
    public List<MoodLog> getMoodHistory(User user) {
        return moodLogRepo.findByUserIdOrderByLogDateDesc(user.getId());
    }

}
