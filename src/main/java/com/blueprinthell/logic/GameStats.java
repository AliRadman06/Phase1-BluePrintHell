package com.blueprinthell.logic;

import com.blueprinthell.controller.WiringController;

public class GameStats {
    private static int totalSize = 0;
    private static int lostCount = 0;
    private static WiringController wiringController;
    private static int coins = 0;


    public static void registerPacket(int packet) {
        totalSize += packet;
    }

    public static void addPacketLoss() {
        lostCount ++;
    }

    public static double getLossPercent() {
        if (totalSize == 0) return 0;
        return lostCount * 100.0 / totalSize;
    }

    public static void reset() {
        totalSize = 0;
        lostCount = 0;
    }

    public static int getLostCount() {
        return lostCount;
    }

    public static int getTotalSize() {
        return totalSize;
    }

    public static boolean isGameOver() {
        return getLossPercent() >= 50.0;
    }

    public static double getRemainingWireLength() {
        return wiringController.getRemainingWires();
    }

    public static double getTemporalProgress() {
        return 0;
    }

    public static double getCoins() {
        return coins;
    }

    public static void addCoins(int c) {
        coins += c;
    }

    public static void resetCoins() {
        coins = 0;
    }
}
