package dev.hollowforge.engine.audio;

import dev.hollowforge.infrastructure.logging.LogManager;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class AudioManager {
    private MediaPlayer currentPlayer;
    private double targetVolume = 0.8;
    private javafx.animation.AnimationTimer fadeTimer;

    public boolean playMusic(String musicPath, int fadeInMs) {
        stopMusic(true);
        URL resource = getClass().getResource(musicPath);
        if (resource == null) {
            LogManager.warning("Recurso no encontrado: " + musicPath);
            return false;
        }
        Media media;
        try {
            media = new Media(resource.toExternalForm());
        } catch (Exception e) {
            LogManager.severe("Error al crear MediaPlayer: " + e.getMessage());
            return false;
        }
        try {
            currentPlayer = new MediaPlayer(media);
            currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            currentPlayer.setVolume(0);
            currentPlayer.play();
            if (fadeInMs > 0) {
                fadeVolume(currentPlayer, 0, targetVolume, fadeInMs);
            } else {
                currentPlayer.setVolume(targetVolume);
            }
            return true;
        } catch (Exception e) {
            System.err.println("[AudioManager] Error al crear MediaPlayer: " + e.getMessage());
            return false;
        }
    }

    public void stopMusic(boolean immediate) {
        if (currentPlayer == null) return;
        if (immediate) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        } else {
            fadeVolume(currentPlayer, currentPlayer.getVolume(), 0, 500);
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        if (currentPlayer != null) {
                            currentPlayer.stop();
                            currentPlayer.dispose();
                            currentPlayer = null;
                        }
                    });
                }
            }, 500);
        }
    }

    public void switchMusic(String newMusicPath, int fadeOutMs, int fadeInMs) {
        if (currentPlayer == null) {
            playMusic(newMusicPath, fadeInMs);
            return;
        }
        fadeVolume(currentPlayer, currentPlayer.getVolume(), 0, fadeOutMs);
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    stopMusic(true);
                    playMusic(newMusicPath, fadeInMs);
                });
            }
        }, fadeOutMs);
    }

    private void fadeVolume(MediaPlayer player, double from, double to, int durationMs) {
        if (player == null) return;
        if (fadeTimer != null) fadeTimer.stop();
        final long start = System.nanoTime();
        final long durationNs = durationMs * 1_000_000L;
        fadeTimer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsed = now - start;
                double t = Math.min(1.0, (double) elapsed / durationNs);
                double newVol = from + (to - from) * t;
                player.setVolume(newVol);
                if (t >= 1.0) {
                    player.setVolume(to);
                    this.stop();
                    fadeTimer = null;
                }
            }
        };
        fadeTimer.start();
    }

    public void setVolume(double volume) {
        this.targetVolume = Math.max(0, Math.min(1, volume));
        if (currentPlayer != null) {
            currentPlayer.setVolume(targetVolume);
            if (fadeTimer != null) {
                fadeTimer.stop();
                fadeTimer = null;
            }
        }
    }

    public double getVolume() {
        return targetVolume;
    }
}
