package com.Zeroloss2.Zeroloss2.service;

import com.Zeroloss2.Zeroloss2.model.Progress;
import com.Zeroloss2.Zeroloss2.repository.ProgressRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalendarService {

    private final ProgressRepository repo;

    public CalendarService(ProgressRepository repo) {
        this.repo = repo;
    }

    private Progress getOrCreateProgress() {
        return repo.findById(1L).orElseGet(() -> {
            Progress p = new Progress();
            p.setId(1L);
            p.setStage(1);
            p.setStreakCount(0);
            p.setDeathJustShown(false);
            p.setLastMessage("まだメッセージはありません。");
            return repo.save(p);
        });
    }

    public Map<String, Object> status() {
        Progress p = getOrCreateProgress();
        LocalDate today = LocalDate.now();

        boolean canPressToday = !today.equals(p.getLastPressDate());

        Map<String, Object> result = new HashMap<>();
        result.put("today", today.getDayOfMonth());
        result.put("stage", p.getStage());
        result.put("streakCount", p.getStreakCount());
        result.put("lastPressDate", p.getLastPressDate());
        result.put("lastMessage", p.getLastMessage());
        result.put("deathJustShown", p.isDeathJustShown());
        result.put("canPressToday", canPressToday);

        return result;
    }

    public Map<String, Object> press(int day, String message) {
        Progress p = getOrCreateProgress();
        LocalDate today = LocalDate.now();

        Map<String, Object> result = new HashMap<>();

        if (day != today.getDayOfMonth()) {
            result.put("ok", false);
            result.put("message", "今日の日付ではありません。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            return result;
        }

        if (today.equals(p.getLastPressDate())) {
            result.put("ok", false);
            result.put("message", "今日はもう押しています。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            return result;
        }

        p.setLastPressDate(today);
        p.setStreakCount(p.getStreakCount() + 1);
        p.setLastMessage(message);

        int stage = p.getStage() + 1;
        if (stage > 6) {
            stage = 6;
        }
        p.setStage(stage);

        repo.save(p);

        result.put("ok", true);
        result.put("message", p.getLastMessage());
        result.put("stage", p.getStage());
        result.put("streakCount", p.getStreakCount());
        result.put("deathJustShown", p.isDeathJustShown());

        return result;
    }
}