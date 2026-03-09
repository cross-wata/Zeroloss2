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

    // DBにProgressが無ければ1件作る
    public Progress getProgress() {
        if (repo.count() == 0) {
            Progress p = new Progress();
            p.setStage(1);
            p.setStreakCount(0);
            p.setDeathJustShown(false);
            p.setLastMessage("");
            repo.save(p);
        }
        return repo.findAll().get(0);
    }

    // 今日の日付
    public int getToday() {
        return LocalDate.now().getDayOfMonth();
    }

    // 初期表示用
    public Map<String, Object> getStatus() {
        Progress p = getProgress();

        Map<String, Object> result = new HashMap<>();
        result.put("today", getToday());
        result.put("stage", p.getStage());
        result.put("streakCount", p.getStreakCount());
        result.put("lastMessage", p.getLastMessage());
        result.put("lastPressDate", p.getLastPressDate());
        result.put("deathJustShown", p.isDeathJustShown());

        return result;
    }

    // ボタン押下時の処理
    public Map<String, Object> press(int day) {
        LocalDate today = LocalDate.now();
        int todayDay = today.getDayOfMonth();

        Map<String, Object> result = new HashMap<>();
        Progress p = getProgress();

        // 当日のボタンしか押せない
        if (day != todayDay) {
            result.put("ok", false);
            result.put("message", "今日は " + todayDay + " 日のボタンだけ押せます。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            return result;
        }

        // 初回
        if (p.getLastPressDate() == null) {
            p.setLastPressDate(today);
            p.setStreakCount(1);
            p.setStage(1);
            p.setDeathJustShown(false);
            p.setLastMessage(day + "日目のメッセージです！");
            repo.save(p);

            result.put("ok", true);
            result.put("message", p.getLastMessage());
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            return result;
        }

        LocalDate last = p.getLastPressDate();

        // 同じ日にもう一度押した場合
        if (today.equals(last)) {
            result.put("ok", true);
            result.put("message", "今日はすでに押しています。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            return result;
        }

        // 連続しているか
        boolean isConsecutive = today.equals(last.plusDays(1));

        // 連続が途切れた場合 → その日は死神(stage6)
        if (!isConsecutive) {
            p.setLastPressDate(today);
            p.setStage(6);
            p.setStreakCount(0);
            p.setDeathJustShown(true);
            p.setLastMessage(day + "日目のメッセージです！");
            repo.save(p);

            result.put("ok", true);
            result.put("message", "連続記録が途切れました。死神が表示されます。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            return result;
        }

        // 死神表示の翌日に押した場合 → stage1で再スタート
        if (p.isDeathJustShown()) {
            p.setLastPressDate(today);
            p.setDeathJustShown(false);
            p.setStreakCount(1);
            p.setStage(1);
            p.setLastMessage(day + "日目のメッセージです！");
            repo.save(p);

            result.put("ok", true);
            result.put("message", "再スタートです。卵(stage1)に戻ります。");
            result.put("stage", p.getStage());
            result.put("streakCount", p.getStreakCount());
            return result;
        }

        // 通常の連続カウント
        p.setLastPressDate(today);
        p.setStreakCount(p.getStreakCount() + 1);

        // 6日連続ごとにstageアップ（1〜5）
        int stage = ((p.getStreakCount() - 1) / 6) + 1;
        if (stage > 5) {
            stage = 5;
        }
        p.setStage(stage);

        p.setLastMessage(day + "日目のメッセージです！");
        repo.save(p);

        result.put("ok", true);
        result.put("message", p.getLastMessage());
        result.put("stage", p.getStage());
        result.put("streakCount", p.getStreakCount());
        return result;
    }
}