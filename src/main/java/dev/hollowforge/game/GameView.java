package dev.hollowforge.game;

import dev.hollowforge.audio.AudioManager;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameView extends StackPane {
    private final StackPane root;
    private PauseMenu pauseMenu;

    public GameView(AudioManager audioManager, Runnable onReturnToMenu, Stage primaryStage) {
        root = new StackPane();

        Rectangle blackRect = new Rectangle();
        blackRect.setFill(Color.BLACK);
        blackRect.widthProperty().bind(widthProperty());
        blackRect.heightProperty().bind(heightProperty());
        Text debugText = new Text("Juego en desarrollo\nPresiona ESC para pausa");
        debugText.setFill(Color.WHITE);
        debugText.setStyle("-fx-font-size: 20px;");

        StackPane gameContent = new StackPane(blackRect, debugText);

        Runnable onResume = () -> {
            pauseMenu.setVisible(false);
            this.setDisable(false);
        };
        pauseMenu = new PauseMenu(audioManager, onResume, onReturnToMenu, primaryStage);
        pauseMenu.setVisible(false);

        root.getChildren().addAll(gameContent, pauseMenu);
        StackPane.setAlignment(pauseMenu, Pos.CENTER);

        getChildren().add(root);
        root.prefWidthProperty().bind(widthProperty());
        root.prefHeightProperty().bind(heightProperty());
    }

    public void togglePauseMenu() {
        boolean visible = pauseMenu.isVisible();
        pauseMenu.setVisible(!visible);
    }

    public boolean isPauseMenuVisible() {
        return pauseMenu.isVisible();
    }

    public void hidePauseMenu() {
        pauseMenu.setVisible(false);
    }
}