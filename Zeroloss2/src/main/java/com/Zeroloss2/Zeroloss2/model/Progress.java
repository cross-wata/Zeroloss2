package com.Zeroloss2.Zeroloss2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Progress {

    @Id
    private Long id;

    private LocalDate lastPressDate;
    private int stage;
    private int streakCount;
    private boolean deathJustShown;
    private String lastMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLastPressDate() {
        return lastPressDate;
    }

    public void setLastPressDate(LocalDate lastPressDate) {
        this.lastPressDate = lastPressDate;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
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