package dev.hollowforge.game;

import dev.hollowforge.audio.AudioManager;
import dev.hollowforge.core.GameLoop;
import dev.hollowforge.util.AppConstants;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameController extends GameLoop {
    private final GameView view;
    private final AudioManager audioManager;
    private final Stage primaryStage;
    private final Runnable onReturnToMenu;
    private boolean paused = false;

    public GameController(AudioManager audioManager, Stage primaryStage, Runnable onReturnToMenu) {
        this.audioManager = audioManager;
        this.primaryStage = primaryStage;
        this.onReturnToMenu = onReturnToMenu;
        this.view = new GameView(audioManager, this::returnToMenu, primaryStage);
        audioManager.switchMusic(AppConstants.MUSIC_GAME, 500, 500);
    }

    private void returnToMenu() {
        stop();
        view.hidePauseMenu();
        onReturnToMenu.run();
    }

    public void setupKeyboard(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    togglePause();
                    break;
                default:
                    break;
            }
        });
    }

    private void togglePause() {
        if (paused) {
            paused = false;
            view.hidePauseMenu();
            start();
        } else {
            paused = true;
            view.togglePauseMenu();
            stop();
        }
    }

    @Override
    protected void update(double delta) {
        if (!paused) {
            // Lógica del juego aquí
        }
    }

    @Override
    protected void render(double alpha) {
        // Renderizado (GameView ya es dinámico)
    }

    public GameView getView() {
        return view;
    }

    public void shutdown() {
        stop();
    }
}