package com.blueprinthell.util;

import com.blueprinthell.logic.GameStats;

public class GameSession {
    private static int currentLevel = 1;
    private static GameStats stats = new GameStats();


    public static void setCurrentLevel(int level) {
        currentLevel = level;
    }

    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static GameStats getStats() {
        return stats;
    }

    public static void resetStats() {
        stats = new GameStats();
    }
}
