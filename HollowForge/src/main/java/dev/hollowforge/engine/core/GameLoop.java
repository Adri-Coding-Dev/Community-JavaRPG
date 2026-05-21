package dev.hollowforge.engine.core;

import javafx.animation.AnimationTimer;

public abstract class GameLoop {
    private AnimationTimer timer;
    private long lastUpdate = 0;
    private final double UPDATE_RATE = 1.0 / 60.0;
    private boolean running = false;

    public void start() {
        if (running) return;
        running = true;
        lastUpdate = System.nanoTime();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!running) return;
                double delta = (now - lastUpdate) / 1_000_000_000.0;
                if (delta > 0.1) delta = 0.1;
                while (delta >= UPDATE_RATE) {
                    update(UPDATE_RATE);
                    delta -= UPDATE_RATE;
                    lastUpdate = now;
                }
                render(delta / UPDATE_RATE);
            }
        };
        timer.start();
    }

    public void stop() {
        running = false;
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    protected abstract void update(double delta);
    protected abstract void render(double alpha);
}
