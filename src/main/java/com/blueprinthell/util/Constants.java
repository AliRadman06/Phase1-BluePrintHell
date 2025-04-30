package com.blueprinthell.util;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Constants {
    static Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    public static double SCREEN_WIDTH = screenBounds.getWidth();
    public static double SCREEN_HEIGHT = screenBounds.getHeight();
}