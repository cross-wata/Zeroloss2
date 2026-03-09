package com.Zeroloss2.Zeroloss2.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate lastPressDate;

    private int streakCount;

    private int stage;

    private boolean deathJustShown;

    private String lastMessage;

    public Long getId() {
        return id;
    }

    public LocalDate getLastPressDate() {
        return lastPressDate;
    }

    public void setLastPressDate(LocalDate lastPressDate) {
        this.lastPressDate = lastPressDate;
    }

    public int getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public boolean isDeathJustShown() {
        return deathJustShown;
    }

    public void setDeathJustShown(boolean deathJustShown) {
        this.deathJustShown = deathJustShown;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}