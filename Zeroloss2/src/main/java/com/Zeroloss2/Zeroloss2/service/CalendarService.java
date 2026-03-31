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
            p.setLastMessage("ようこそあどべんドットかれんだーへ");
            p.setLastPressDate(null);
            return repo.save(p);
        });
    }

    private boolean isStreakBroken(LocalDate lastDate, LocalDate today) {
        if (lastDate == null) {
            return false;
        }

        // 月が変わっていたら月初リセットを優先するのでここでは除外
        if (isNewMonth(lastDate, today)) {
            return false;
        }

        // 昨日以外なら連続切れ
        return !lastDate.equals(today.minusDays(1));
    }

    private void setDeathStage(Progress p) {
        p.setStage(7);
        p.setDeathJustShown(true);
        p.setLastMessage("れんぞくが とぎれてしまいました……。");
    }

    private void restartFromBeginning(Progress p, LocalDate today, String message) {
        p.setLastPressDate(today);
        p.setStreakCount(1);
        p.setLastMessage(message);
        p.setStage(calculateStage(1)); // 1日目なので stage2
        p.setDeathJustShown(false);
    }

    /**
     * stage計算
     * 0回   -> stage1
     * 1～6回 -> stage2
     * 7～12回 -> stage3
     * 13～18回 -> stage4
     * 19～24回 -> stage5
     * 25～30回 -> stage6
     * 31回以上 -> stage7
     */
    private int calculateStage(int streakCount) {
        if (streakCount <= 0) {
            return 1;
        }

        int stage = 2 + (streakCount - 1) / 6;

        if (stage > 7) {
            stage = 7;
        }

        return stage;
    }

    /**
     * 月が変わったか判定
     */
    private boolean isNewMonth(LocalDate lastDate, LocalDate today) {
        if (lastDate == null) {
            return false;
        }

        return lastDate.getYear() != today.getYear()
                || lastDate.getMonthValue() != today.getMonthValue();
    }

    /**
     * 月が変わったときのリセット
     */
    private void resetForNewMonth(Progress p) {
        p.setStreakCount(0);
        p.setStage(1);
        p.setDeathJustShown(false);
        p.setLastMessage("新しい月が始まりました。");
        p.setLastPressDate(null);
    }

    public Map<String, Object> status() {
        Progress p = getOrCreateProgress();
        LocalDate today = LocalDate.now();

        // 月が変わっていたらリセット
        if (isNewMonth(p.getLastPressDate(), today)) {
            resetForNewMonth(p);
            repo.save(p);
        }

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

        // 月が変わっていたらリセット
        if (isNewMonth(p.getLastPressDate(), today)) {
            resetForNewMonth(p);
            repo.save(p);
        }

        // 今日の日付以外は押せない
        if (day != today.getDayOfMonth()) {
            result.put("ok", false);
            result.put("message", "今日の日付ではありません。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            result.put("deathJustShown", p.isDeathJustShown());
            return result;
        }

        // 同じ日は2回押せない
        if (today.equals(p.getLastPressDate())) {
            result.put("ok", false);
            result.put("message", "今日はもう押しています。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            result.put("deathJustShown", p.isDeathJustShown());
            return result;
        }

        // 前回死神を表示した直後なら、今回は最初から再開
        if (p.isDeathJustShown()) {
            restartFromBeginning(p, today, message);
            repo.save(p);

            result.put("ok", true);
            result.put("message", p.getLastMessage());
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            result.put("deathJustShown", p.isDeathJustShown());
            return result;
        }

        // 連続が途切れていたら、今回は死神表示だけする
        if (isStreakBroken(p.getLastPressDate(), today)) {
            p.setLastPressDate(today);
            p.setStreakCount(0);
            setDeathStage(p);

            repo.save(p);

            result.put("ok", true);
            result.put("message", p.getLastMessage());
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            result.put("deathJustShown", p.isDeathJustShown());
            return result;
        }

        // 通常の連続押下
        p.setLastPressDate(today);
        p.setStreakCount(p.getStreakCount() + 1);
        p.setLastMessage(message);
        p.setStage(calculateStage(p.getStreakCount()));
        p.setDeathJustShown(false);

        repo.save(p);

        result.put("ok", true);
        result.put("message", p.getLastMessage());
        result.put("stage", p.getStage());
        result.put("streakCount", p.getStreakCount());
        result.put("deathJustShown", p.isDeathJustShown());

        return result;
    }
}