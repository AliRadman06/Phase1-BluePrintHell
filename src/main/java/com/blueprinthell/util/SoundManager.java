package com.blueprinthell.util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer mediaPlayer;
    private double volume = 0.5;
    private final Map<String, AudioClip> effects = new HashMap<>();
    private double effectVolume = 0.7;


    private SoundManager() {}

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playMusic(String path) {
        if (mediaPlayer != null) {
            return;
        }
        try {
            Media media = new Media(getClass().getResource(path).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(volume);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public double getVolume() {
        return volume;
    }

    public void pause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    public void resume() {
        if (mediaPlayer != null) mediaPlayer.play();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public void playEffect(String name, String path) {
        AudioClip clip = effects.computeIfAbsent(name, n ->
                new AudioClip(getClass().getResource(path).toExternalForm())
        );
        clip.setVolume(effectVolume);
        clip.play();
    }

    public void setEffectVolume(double vol) { effectVolume = vol; }
    public double getEffectVolume() { return effectVolume; }
}
