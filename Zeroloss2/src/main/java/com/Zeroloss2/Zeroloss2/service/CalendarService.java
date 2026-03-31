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
        p.setStage(14);
        p.setDeathJustShown(true);
        p.setLastMessage("れんぞくが とぎれてしまいました……。");
    }

    private void restartFromBeginning(Progress p, LocalDate today, String message) {
        p.setLastPressDate(today);
        p.setStreakCount(1);
        p.setLastMessage(message);
        p.setStage(calculateStage(0)); // ← stage1になる
        p.setDeathJustShown(false);
    }

    /**
     * stage計算
     *
     * 0日目  -> stage1
     * 1日目  -> stage2
     * 3日目  -> stage3
     * 5日目  -> stage4
     * 7日目  -> stage5
     * 9日目  -> stage6
     * 11日目 -> stage7
     * 13日目 -> stage8
     * 15日目 -> stage9
     * 17日目 -> stage10
     * 19日目 -> stage11
     * 22日目 -> stage12
     * 27日目 -> stage13
     *
     * ※ stage14 は死神専用
     */
    private int calculateStage(int streakCount) {
        if (streakCount <= 0) {
            return 1;
        }

        int[] thresholds = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 22, 27};

        for (int i = thresholds.length - 1; i >= 0; i--) {
            if (streakCount >= thresholds[i]) {
                return i + 2;
            }
        }

        return 1;
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